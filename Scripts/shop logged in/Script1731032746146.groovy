import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable

import static org.assertj.core.api.InstanceOfAssertFactories.map

import org.openqa.selenium.Keys as Keys

//search keyword
String searchKeyword = 'Flying Ninja'

//user billing details
//company, street, phone changes each try, can't verify
//email too, but it seems to only change once each day
Map billingDetails = [
	"firstname": "katalon",
	"lastname": "customer",
//	"company": "DXC",
	"country": "VIETNAM",
//	"street": "628 Nguyen Hai Phong",
	"postcode": "70000",
	"city": "HCM",
//	"phone": "0289928289",
	"email": "katalonLover@outlook.com"
]

//ship to different address?
boolean isShipDiff = false

//shipping details - assume same as billing details for now

//delivery notes
String note = new String()

//item amount
Integer amount = 3

WebUI.callTestCase(findTestCase('login valid cred'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Shop Page/menu Shop'))

WebUI.setText(findTestObject('Shop Page/search bar'), searchKeyword)

WebUI.click(findTestObject('Shop Page/search icon'))

WebUI.verifyElementText(findTestObject('Search Result/h1 search results for item'), 
    "Search Results for: $searchKeyword")

WebUI.click(findTestObject('Search Result/first result thumbnail'))

WebUI.verifyElementText(findTestObject('Item Page/h1 item name'), searchKeyword)

WebUI.setText(findTestObject('Object Repository/Item Page/input amount'), amount.toString())

WebUI.click(findTestObject('Item Page/button add to cart'))

//has been added msg textnya ga bisa ketemu pas run automation, jadi cek present elementnya aja dulu
WebUI.verifyElementPresent(findTestObject('Object Repository/Item Page/div has been added msg'), 3)

//go to cart
WebUI.click(findTestObject('Shop Page/menu Cart'))

//verify item name and amount in cart page
WebUI.verifyElementText(findTestObject('Cart Page/first product name in cart list'), searchKeyword)

//WebUI.verifyElementText(findTestObject('Cart Page/first product quantity in cart list'), 
//    amount.toString())
//pakai verifyElementAttributeValue karena jumlah item bukan text tapi disimpan di attribute "value" di elementnya
WebUI.verifyElementAttributeValue(findTestObject('Cart Page/first product quantity in cart list'), "value", amount.toString(), 0)

//go to checkout and verify page header is now "Checkout"
WebUI.click(findTestObject('Cart Page/button proceed to checkout'))
WebUI.verifyElementText(findTestObject('Checkout Page/h1 Checkout'), 'Checkout')

//verify billing details
//company, street, phone not checked because they change every time
WebUI.verifyElementAttributeValue(findTestObject('Checkout Page/input billing first name'), "value", billingDetails.firstname, 0)

WebUI.verifyElementAttributeValue(findTestObject('Checkout Page/input billing last name'), "value", billingDetails.lastname, 0)

WebUI.verifyElementText(findTestObject('Checkout Page/input billing country dropdown'), billingDetails.country)

WebUI.verifyElementAttributeValue(findTestObject('Checkout Page/input billing postcode (optional)'), "value", billingDetails.postcode, 0)

WebUI.verifyElementAttributeValue(findTestObject('Checkout Page/input billing city'), "value", billingDetails.city, 0)

WebUI.verifyElementAttributeValue(findTestObject('Checkout Page/input billing email'), "value", billingDetails.email, 0)

//input notes if available
if (!note.blank) {
	WebUI.setText(findTestObject('Object Repository/Checkout Page/input notes (optional)'), amount)
}

//check if shipped to different address
if (isShipDiff) {
	WebUI.click(findTestObject('Object Repository/Checkout Page/checkbox ship to diff addr'))
	//input shipping details
}

//verify order review (simbol x nya bukan x biasa)
WebUI.verifyElementText(findTestObject('Object Repository/Checkout Page/div review product name'), "${searchKeyword}  × ${amount}")

//place order
WebUI.click(findTestObject('Object Repository/Checkout Page/button place order'))

//verify order received - thank you
WebUI.verifyElementText(findTestObject('Object Repository/Checkout Page/p thank you'), "Thank you. Your order has been received.")
//verify order received - order details (&nbsp; atau \u00a0 di htmlnya kedetect tapi saat katalon cek diubah jadi space biasa,
//yang masalah simbol x nya bukan simbol x biasa tapi pakai \u00d7)
WebUI.verifyElementText(findTestObject('Object Repository/Checkout Page/name and quantity in product details'), "${searchKeyword} \u00d7 ${amount}")
//verify order received - billing address
String billingAddr = WebUI.getText(findTestObject('Object Repository/Checkout Page/billing address'))
boolean isBillAddrCorrect = billingAddr.contains(billingDetails.firstname) && 
	billingAddr.contains(billingDetails.lastname) && 
	billingAddr.contains(billingDetails.firstname) &&
	billingAddr.contains(billingDetails.city) &&
	billingAddr.containsIgnoreCase(billingDetails.country)
if (!isBillAddrCorrect) {
	KeywordUtil.markError("Billing address incorrect")
}

WebUI.closeBrowser()