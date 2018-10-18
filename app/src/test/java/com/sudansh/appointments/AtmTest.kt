package com.sudansh.appointments



//@RunWith(JUnit4::class)
//class AtmTest {
//
//    @Mock
//    val bankService = mock(BankService::class.java)
//    @Mock
//    private val cashDispenser: CashDispenser? = null
//    val atm = Atm(bankService, cashDispenser)
//
//    @Throws(NumberFormatException::class)
//    @Test
//    fun test_valid_pin() {
//        val pin = Integer.parseInt(atm.getPin().trim())
//        assert(pin in 1001..9999)
//    }
//
//    @Test
//    fun test_authorize() {
//        //given
//        `when`(bankService.authorizeUser(anyInt())).thenReturn("success")
//
//        //when
//        atm.sendPin()
//
//        atm.author
//    }
//
//    @Test
//    fun testIsTransactionPossible() {
//        val amount = 100
//        val balance = 300
//
//        assertTrue(
//            "balance > amount",
//            sut.isTransactionPossible(amount, balance)
//        )
//        assertTrue(
//            "balance == amount",
//            sut.isTransactionPossible(balance, balance)
//        )
//        assertFalse(
//            "balance < amount",
//            sut.isTransactionPossible(balance, amount)
//        )
//    }
//    @Test
//    fun testWithdrawCashNotEnoughMoneyOnAccount() {
//        //given
//        val amount = BigDecimal("1000")
//        val balance = BigDecimal("500")
//        Mockito.`when`(keypad.getAmount()).thenReturn(amount)
//        Mockito.`when`(bankService.getBalance()).thenReturn(balance)
//        Mockito.`when`(cashDispenser.isThereEnoughMoney(amount)).thenReturn(true)
//
//        //when
//        sut.withdrawCash()
//
//        //then
//        Mockito.verifyZeroInteractions(cashDispenser)
//        //http://stackoverflow.com/questions/1778744/using-mockitos-generic-any-method
//        Mockito.verify(bankService, Mockito.times(0))
//            .debit(Mockito.any<BigDecimal>(BigDecimal::class.java!!))
//    }
//
//}