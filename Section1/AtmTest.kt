import android.provider.BlockedNumberContract.isBlocked
import org.mockito.Mockito.`when`
import android.accounts.WebService
import javax.smartcardio.Card
import org.mockito.Mockito.inOrder
import org.mockito.InOrder

/**
 *
 */
class AtmTest {

    @Test(expected = IllegalArgumentException::class)
    fun testInputNegativeAmountOfMoneyInATM() {
        println("checkATMForNegativeValueInput")
        val atmTest = ATM(-100.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testATMForOverflowInput() {
        println("checkATMForOverflowInput")
        val atmTest = ATM(999999999)
    }

    @Test
    fun testGetMoneyInATM() {
        println("getMoneyInATM")
        val atmTest = ATM(100.0)
        val expResult = 100.0
        val result = atmTest.getMoneyInATM()
        assertEquals(expResult, result, 0.0)
    }

    @Test
    fun testDepositMoney() {
        println("depositMoneyInATM")
        val atmTest = ATM(100.0)
        atm.deposit(200)
        val expResult = 300.0
        val result = atmTest.getMoneyInATM()
        assertEquals(expResult, result)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testInsertMoreThanOneCard() {
        println("insertMoreThanOneCard")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        atmTest.insertCard(mockCard)
        atmTest.insertCard(mockCard)
    }

    @Test
    @Throws(DailyLimitException::class)
    fun testDailyLimitExceed() {
        println("dailyLimitExceed")
        val atmTest = ATM(1000.0)
        atm.getCash(100)
        atm.getCash(150)
        verify(atm.withdraw(300), times(0))
    }

    @Test
    fun testInsertOnlyOneCard() {
        println("insertOnlyOneCard")
        val atmTest = ATM(0.0)
        val mockCard = mock(Card::class.java)
        val result = atmTest.insertCard(mockCard)
        assertTrue(result)
    }

    @Test
    @Throws(NoCardInsertedException::class)
    fun testValidateBlockedCard() {
        println("validateBlockedCard")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val pinCode = 7777
        `when`(mockCard.isBlocked()).thenReturn(true)
        `when`(mockCard.checkPin(pinCode)).thenReturn(true)
        assertFalse(atmTest.validateCard(mockCard, pinCode))
        verify(mockCard).isBlocked()
    }

    @Test
    @Throws(NoCardInsertedException::class)
    fun testValidateCardWithInvalidPin() {
        println("validateCardWithInvalidPin")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val pinCode = 7777
        `when`(mockCard.isBlocked()).thenReturn(false)
        `when`(mockCard.checkPin(pinCode)).thenReturn(false)
        assertFalse(atmTest.validateCard(mockCard, pinCode))
    }

    @Test(expected = NullPointerException::class)
    @Throws(NoCardInsertedException::class)
    fun testValidateCardNullCard() {
        println("validateNullCard")
        val atmTest = ATM(100)
        val pinCode = 7777
        atmTest.validateCard(null, pinCode)
    }

    @Test
    @Throws(NoCardInsertedException::class)
    fun testValidateValidCard() {
        println("validateValidCard")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val pinCode = 7777
        `when`(mockCard.isBlocked()).thenReturn(false)
        `when`(mockCard.checkPin(pinCode)).thenReturn(true)
        val expResult = true
        val result = atmTest.validateCard(mockCard, pinCode)
        assertEquals(expResult, result)
        val inOrder = inOrder(mockCard)
        inOrder.verify(mockCard).isBlocked()
        inOrder.verify(mockCard).checkPin(pinCode)
    }

    @Test
    @Throws(NoCardInsertedException::class)
    fun testCheckBalance() {
        println("checkBalance")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val mockAccount = mock(WebService::class.java)
        atmTest.insertCard(mockCard)
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        `when`(mockAccount.getBalance()).thenReturn(0.0)
        val expResult = 0.0
        val result = atmTest.checkBalance()
        assertEquals(expResult, result, 0.0)
        val inOrder = inOrder(mockCard, mockAccount)
        inOrder.verify(mockCard).getAccount()
        inOrder.verify(mockAccount).getBalance()
    }

    @Test(expected = IllegalArgumentException::class)
    @Throws(
        NotEnoughMoneyInATMException::class,
        NotEnoughMoneyInAccountException::class,
        NoCardInsertedException::class
    )
    fun testGetCashLessThanZero() {
        println("getCashLessThanZero")
        val amount = -100.0
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        atmTest.insertCard(mockCard)
        val mockAccount = mock(WebService::class.java)
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        atmTest.getCash(amount)
    }

    @Test(expected = NotEnoughMoneyInATMException::class)
    @Throws(
        NoCardInsertedException::class,
        NotEnoughMoneyInAccountException::class,
        NotEnoughMoneyInATMException::class
    )
    fun testGetCashNotEnoughMoneyInATM() {
        println("getCashNotEnoughMoneyInATM")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val mockAccount = mock(WebService::class.java)
        val amount = 1100.0
        val pinCode = 7777
        atmTest.insertCard(mockCard)
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        `when`(mockCard.checkPin(pinCode)).thenReturn(true)
        atmTest.validateCard(mockCard, pinCode)
        atmTest.getCash(amount)
        val inOrder = inOrder(mockCard, mockAccount)
        inOrder.verify(mockCard).getAccount()
        inOrder.verify(mockAccount).getBalance()
    }

    @Test(expected = NotEnoughMoneyInAccountException::class)
    @Throws(
        NoCardInsertedException::class,
        NotEnoughMoneyInAccountException::class,
        NotEnoughMoneyInATMException::class
    )
    fun testGetCashNotEnoughMoneyInAccount() {
        println("getCashNotEnoughMoneyInAccount")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        val mockAccount = mock(WebService::class.java)
        val pinCode = 7777
        val balance = 200.0
        val amount = 500.0
        atmTest.insertCard(mockCard)
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        `when`(mockAccount.getBalance()).thenReturn(balance)
        `when`(mockCard.checkPin(pinCode)).thenReturn(true)
        `when`(mockCard.getAccount().withdrow(amount)).thenReturn(balance - amount)
        atmTest.validateCard(mockCard, pinCode)
        atmTest.getCash(amount)
        val inOrder = inOrder(mockCard, mockAccount)
        inOrder.verify(mockCard).getAccount()
        inOrder.verify(mockAccount).getBalance()
    }

    @Test
    @Throws(
        NotEnoughMoneyInAccountException::class,
        NotEnoughMoneyInATMException::class,
        NoCardInsertedException::class
    )
    fun testGetCash() {
        println("getCashNotZeroBalance")
        val atmMoney = 1000.0
        val atmTest = ATM(atmMoney)
        val mockCard = mock(Card::class.java)
        val mockAccount = mock(WebService::class.java)
        atmTest.insertCard(mockCard)
        val balance = 600.0
        val amount = 100.0
        val pinCode = 7777
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        `when`(mockCard.checkPin(pinCode)).thenReturn(true)
        `when`(mockCard.isBlocked()).thenReturn(false)
        `when`(mockAccount.getBalance()).thenReturn(balance)
        `when`(mockAccount.withdrow(amount)).thenReturn(amount)
        atmTest.validateCard(mockCard, pinCode)
        atmTest.getCash(amount)
        `when`(mockAccount.getBalance()).thenReturn(balance - amount)
        assertEquals(atmTest.getMoneyInATM(), atmMoney - amount, 0.0)
        assertEquals(atmTest.checkBalance(), balance - amount, 0.0)
        val inOrder = inOrder(mockCard, mockAccount)
        inOrder.verify(mockCard).isBlocked()
        inOrder.verify(mockCard).checkPin(pinCode)
        inOrder.verify(mockCard, atLeastOnce()).getAccount()
        verify(mockAccount).withdrow(amount)
        inOrder.verify(mockAccount).getBalance()
    }

    @Test(expected = NoCardInsertedException::class)
    @Throws(
        NotEnoughMoneyInATMException::class,
        NotEnoughMoneyInAccountException::class,
        NoCardInsertedException::class
    )
    fun testGetCardFromATMIfCardIsNotInserted() {
        println("getCardFromATMIfCardIsNotInserted")
        val atmTest = ATM(1000.0)
        atmTest.getCardFromATM()
    }

    @Test(expected = NoCardInsertedException::class)
    @Throws(NoCardInsertedException::class)
    fun testGetCardFromATMMoreThanOneTime() {
        println("getCardFromATMMoreThanOneTime")
        val atmTest = ATM(1000.0)
        val mockCard = mock(Card::class.java)
        atmTest.insertCard(mockCard)
        atmTest.getCardFromATM()
        atmTest.getCardFromATM()
    }

    @Test
    @Throws(NoCardInsertedException::class)
    fun testCheckBalanceVerify() {
        println("checkBalanceVerify")
        val atmTest = ATM(1000)
        val mockCard = mock(Card::class.java)
        val mockAccount = mock(WebService::class.java)
        `when`(mockCard.getAccount()).thenReturn(mockAccount)
        atmTest.insertCard(mockCard)
        atmTest.checkBalance()

        //only one operation allowed per card insert
        verify(mockCard, times(1)).isBlocked()
    }

}