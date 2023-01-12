
Step 1: Create accounts 
at Node A
```
flow start CreateNewAccountAndShare acctName: bob6424, node2: ParticipantB, node3: ParticipantC
```
at Node B
```
flow start CreateNewAccountAndShare acctName: Nancy3426, node2: ParticipantA, node3: ParticipantC
```
at Node B
```
flow start CreateNewAccountAndShare acctName: Peter7548, node2: ParticipantB, node3: ParticipantA
```

Step 2: create IOU from node A
view all accounts -> get the account ids -> issue IOU
```
flow start ViewAccounts
flow start IOUIssueFlow meID: 07649a16-5d9f-489b-bab0-1cb7ccc14e2c, lenderID: 027df016-fc4b-4774-9c58-183032918370, amount: 20
```

