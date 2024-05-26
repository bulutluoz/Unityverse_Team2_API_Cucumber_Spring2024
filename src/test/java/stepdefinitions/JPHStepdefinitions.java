package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import utilities.ConfigReader;

import java.lang.module.ResolutionException;

public class JPHStepdefinitions {

    public String endpoint="";
    Response actualResponse;
    JsonPath actualResponseJpath;
    JSONObject postRequestBody;
    static JSONObject putRequestBody;
    JSONObject expectedResponseBody;

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

    @Then("POST request icin {string},{string},{int} bilgileri ile request body olusturur")
    public void post_request_icin_bilgileri_ile_request_body_olusturur(String title, String body, Integer userId) {

        postRequestBody = new JSONObject();
        postRequestBody.put("title",title);
        postRequestBody.put("body",body);
        postRequestBody.put("userId",userId);
    }
    @Then("jPH server a POST request gonderir ve testleri yapmak icin response degerini kaydeder")
    public void j_ph_server_a_post_request_gonderir_ve_testleri_yapmak_icin_response_degerini_kaydeder() {

        actualResponse = RestAssured
                                .given().contentType(ContentType.JSON)
                                .when().body(postRequestBody.toString())
                                .post(endpoint);
    }
    @Then("jPH respons daki {string} header degerinin {string}")
    public void j_ph_respons_daki_header_degerinin(String verilenHeader, String expectedHeaderDegeri) {

        actualResponse
                .then()
                .assertThat()
                .header(verilenHeader,expectedHeaderDegeri);
    }
    @Then("response attribute degerlerinin {string},{string},{int} ve {int} oldugunu test eder")
    public void response_attribute_degerlerinin_ve_oldugunu_test_eder(String expectedTitle, String expectedBody, Integer expectedUserId, Integer expectedId) {

        /*
        {
            "title": "Ahmet",
            "body": "Merhaba",
            "userId": 10,
            "id": 101
        }
         */

        // Elimizde response var
        // response objesinden attribute degerlerini almak icin
        // Jsonpath'e cevirmeliyiz

        actualResponseJpath = actualResponse.jsonPath();

        Assert.assertEquals(expectedTitle,actualResponseJpath.getString("title"));
        Assert.assertEquals(expectedBody,actualResponseJpath.getString("body"));
        Assert.assertEquals(expectedUserId,(Integer)actualResponseJpath.getInt("userId"));
        Assert.assertEquals(expectedId,(Integer)actualResponseJpath.getInt("id"));


    }

    @When("PUT request icin {string} {string} ve {int} degerleri ile request body olusturur")
    public void put_request_icin_ve_degerleri_ile_request_body_olusturur(String title, String body, Integer userId) {
        putRequestBody = new JSONObject();
        putRequestBody.put("title",title);
        putRequestBody.put("body",body);
        putRequestBody.put("userId",userId);
    }
    @When("Test icin  {string} {string} {int} ve {int} degerleri ile expected response body olusturur")
    public void test_icin_ve_degerleri_ile_expected_response_body_olusturur(String expectedTitle, String expectedBody, Integer expectedUserId, Integer expectedId) {
        //{
        //    "title": "Son Test",
        //    "body": "API veda",
        //    "userId": 10,
        //    "id": 77
        //}
        expectedResponseBody = new JSONObject();
        expectedResponseBody.put("title",expectedTitle);
        expectedResponseBody.put("body",expectedBody);
        expectedResponseBody.put("userId",expectedUserId);
        expectedResponseBody.put("id",expectedId);
    }
    @Then("jPH server'a PUT request gonderir ve response'i kaydeder")
    public void j_ph_server_a_put_request_gonderir_ve_response_i_kaydeder() {
        actualResponse = RestAssured
                                    .given().contentType(ContentType.JSON)
                                    .when().body(putRequestBody.toString())
                                    .put(endpoint);

    }
    @Then("expected response body ile actual response'daki attribute degerlerinin ayni oldugunu test eder")
    public void expected_response_body_ile_actual_response_daki_attribute_degerlerinin_ayni_oldugunu_test_eder() {
        // expectedResponseBody (JSONObject) <==> actualResponse (Jsonpath'e cevirecegiz)

        actualResponseJpath = actualResponse.jsonPath();

        Assert.assertEquals(
                expectedResponseBody.getString("title"),
                actualResponseJpath.getString("title")
                );

        Assert.assertEquals(
                expectedResponseBody.getString("body"),
                actualResponseJpath.getString("body")
        );

        Assert.assertEquals(
                expectedResponseBody.getInt("userId"),
                actualResponseJpath.getInt("userId")
        );

        Assert.assertEquals(
                expectedResponseBody.getInt("id"),
                actualResponseJpath.getInt("id")
        );


    }
}
