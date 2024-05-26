package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import utilities.ConfigReader;

import java.lang.module.ResolutionException;

public class JPHStepdefinitions {

    public String endpoint="";
    Response actualResponse;
    JsonPath actualResponseJpath;

    @Given("Kullanici {string} base URL'ini kullanir")
    public void kullanici_base_url_ini_kullanir(String configdekiBaseUrl) {
        endpoint += ConfigReader.getProperty(configdekiBaseUrl);
        //https://jsonplaceholder.typicode.com/
    }
    @Then("Path parametreleri icin {string} kullanir")
    public void path_parametreleri_icin_kullanir(String verilenPathParametreleri) {
        endpoint += verilenPathParametreleri;
        // https://jsonplaceholder.typicode.com/posts/44
    }
    @Then("jPH server a GET request gonderir ve testleri yapmak icin response degerini kaydeder")
    public void j_ph_server_a_get_request_gonderir_ve_testleri_yapmak_icin_response_degerini_kaydeder() {

        actualResponse = RestAssured
                                        .given()
                                        .when()
                                        .get(endpoint);
    }
    @Then("jPH response'da status degerinin {int}")
    public void j_ph_response_da_status_degerinin(Integer expectedStatusCode) {

        actualResponse.then().assertThat().statusCode(expectedStatusCode);
    }
    @Then("jPH response'da content type degerinin {string}")
    public void j_ph_response_da_content_type_degerinin(String expectedContentType) {
        actualResponse.then().assertThat().contentType(expectedContentType);
    }
    @Then("jPH responseBody'sindeki attributeleri test etmek icin response i JsonPath objesine cast eder")
    public void j_ph_response_body_sindeki_attributeleri_test_etmek_icin_response_i_json_path_objesine_cast_eder() {
        actualResponseJpath = actualResponse.jsonPath();
    }
    @Then("jPH GET response body'sinde {string} degerinin Integer {int}")
    public void j_ph_get_response_body_sinde_degerinin_integer(String verilenAttribute, Integer intAtrValue) {
        // expected deger (int)  <==> actualResponseJpath'deki attribute'un degerini
        Assert.assertEquals(intAtrValue, (Integer) actualResponseJpath.getInt(verilenAttribute));

    }
    @Then("jPH GET response body'sinde {string} degerinin String {string}")
    public void j_ph_get_response_body_sinde_degerinin_string(String verilenAttribute, String strAtrValue) {
        Assert.assertEquals(strAtrValue,actualResponseJpath.getString(verilenAttribute));
    }
}
