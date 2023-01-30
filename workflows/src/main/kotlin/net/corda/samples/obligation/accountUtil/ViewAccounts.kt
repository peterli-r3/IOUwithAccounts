package net.corda.samples.obligation.accountUtil



import net.corda.core.flows.*
import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.accounts.workflows.accountService
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.finance.contracts.asset.Cash
import net.corda.samples.obligation.states.IOUState


@StartableByRPC
@StartableByService
@InitiatingFlow
class ViewAccounts() : FlowLogic<List<String>>() {

    @Suspendable
    override fun call(): List<String> {
        //Create a new account
        val aAccountsQuery = accountService.allAccounts().map {"\n"+it.state.data.name + " from host[" + it.state.data.host+ "]" + ", id: "+ it.state.data.identifier.id}
        return aAccountsQuery
    }
}

@StartableByRPC
@StartableByService
@InitiatingFlow
class ViewCashBalanceByAccount(
    val acctname : String
) : FlowLogic<String>() {

    @Suspendable
    override fun call(): String {

        val myAccount = accountService.accountInfo(acctname).single().state.data
        val criteria = QueryCriteria.VaultQueryCriteria(
            externalIds = listOf(myAccount.identifier.id)
        )
        var totalBalance :Long = 0
        val cash = serviceHub.vaultService.queryBy(
            contractStateType = Cash.State::class.java,
            criteria = criteria
        ).states.map {
            totalBalance += it.state.data.amount.quantity
            "\n" + "cash balance: " + it.state.data.amount
        }
        totalBalance /= 100
        return "\nTotal Balance : $totalBalance USD.\nEach money drop is: $cash"
    }

}

@StartableByRPC
@StartableByService
@InitiatingFlow
class ViewIOUByAccount(
    val acctname : String
) : FlowLogic<String>() {

    @Suspendable
    override fun call(): String {

        val myAccount = accountService.accountInfo(acctname).single().state.data
        val name =myAccount.name
        val criteria = QueryCriteria.VaultQueryCriteria(
            externalIds = listOf(myAccount.identifier.id)
        )
        val ious = serviceHub.vaultService.queryBy(
            contractStateType = IOUState::class.java,
            criteria = criteria
        ).states.map {
            val lenderAccount = accountService.accountInfo(it.state.data.lenderAcctID)!!.state.data

            "\n" + "iou: " + it.state.data.amount + " | borrowed from acct: " + lenderAccount.name +"(Node: "+ lenderAccount.host.name.organisation+") | Paid : " + it.state.data.paid
        }
        return "\n$name has iou is: $ious"
    }

}

//flow start ViewIOUByAccount acctname: bob6424


//flow start ViewCashBalanceByAccount acctname: bob6424

//flow start ViewAccounts
//flow start IOUIssueFlow meID: 36ea16ec-a089-49a3-8e85-3cd90809ae27, lenderID: 34990634-1b8f-4fe1-aebe-a315a81d0b71, amount: 20
//flow start IOUSettleFlow linearId: e3378fc7-7fbb-4477-9d73-3c44f2577c8c, meID: 36ea16ec-a089-49a3-8e85-3cd90809ae27, settleAmount: 5
//flow start IOUTransferFlow linearId: e3378fc7-7fbb-4477-9d73-3c44f2577c8c, meID: 34990634-1b8f-4fe1-aebe-a315a81d0b71, newLenderID: afc9f98f-ef58-402d-9d3d-c4774191e661

//flow start IOUTransferFlow linearId: d4487a5b-45ee-4804-9350-55f1286d8f79, meID: 027df016-fc4b-4774-9c58-183032918370, newLenderID: 171f706a-97f6-4566-9aa4-f6be2940de1a