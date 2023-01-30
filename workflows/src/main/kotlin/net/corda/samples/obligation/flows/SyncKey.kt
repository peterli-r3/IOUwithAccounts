package net.corda.samples.obligation.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.ci.workflows.SyncKeyMappingFlow
import com.r3.corda.lib.ci.workflows.SyncKeyMappingFlowHandler
import net.corda.core.flows.*
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party


@InitiatingFlow
class SyncKey (
    private val party: Party,
    private val keys: List<AbstractParty>
    ): FlowLogic<Unit>() {
    override fun call(): Unit {
        return subFlow(SyncKeyMappingFlow(initiateFlow(party),keys))
    }

}

@InitiatedBy(SyncKey::class)
class SyncKeyResponder(val flowSession: FlowSession): FlowLogic<Unit>() {
    @Suspendable
    override fun call(): Unit {
        return subFlow(SyncKeyMappingFlowHandler(flowSession));
    }
}