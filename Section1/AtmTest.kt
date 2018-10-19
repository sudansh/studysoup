class AtmTest {

    @Mock
    val webservice = mock(WebService::class.java)
    @Mock
    private val cashDispenser: CashDispenser? = null
    @Mock
    private val keyPad: KeyPad? = null
    val atm = Atm(webservice, cashDispenser, keypad)

    @Throws(NumberFormatException::class)
    @Test
    fun test_valid_pin() {
        val pin = Integer.parseInt(keyPad.getPin().trim())
        assert(pin in 1000..9999)
    }

    @Test
    fun test_authorize() {
        val cardNumber = 1234567890
        val pin = 1234
        val result = webservice.authorize(cardNumber, pin)
        assertTrue(result)
    }

    @Test
    fun test_get_balance(){
        val balance = webservice.getBalance()
        assertTrue(atm.getBalance(), balance)
    }

    @Test
    fun test_invalid_withdraw() {
        val amount = 100
        val balance = 300

        assertTrue(atm.withdraw(),)
    }

}