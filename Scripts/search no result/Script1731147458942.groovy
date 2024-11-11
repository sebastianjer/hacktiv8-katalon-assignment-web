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
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

//search keyword that will have no result
String searchKeyword = 'Gundam'

Mobile.callTestCase(findTestCase('open page'), [:], FailureHandling.STOP_ON_FAILURE)

//just to make sure we're on Shop page
WebUI.click(findTestObject('Shop Page/menu Shop'))

WebUI.setText(findTestObject('Shop Page/search bar'), searchKeyword)

WebUI.click(findTestObject('Shop Page/search icon'))

//verify text searched is the one we inputted
WebUI.verifyElementText(findTestObject('Search Result/h1 search results for item'),
	"Search Results for: $searchKeyword")

//verify nothing is found
WebUI.verifyElementText(findTestObject('Search Result/h1 nothing found'), "Nothing Found")