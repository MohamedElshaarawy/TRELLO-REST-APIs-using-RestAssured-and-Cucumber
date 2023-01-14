package stepDefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

public class EndToEndTrello {


    static String baseURL = "https://api.trello.com";
    static String key = "7ea170d905be329a63ae62bb2e47e128";
    static String token = "ATTA3e683e3a7c2f5eebb9486bcdec83b6717525bcfa8ef099889d3ea2fefac6bcb60A3BB855";
    static String organizationId = "";
    static String listId = "";
    static String boardId = "";

    @Given("create a new organization")
    public void createNewOrganiazation() throws PendingException {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                basePath("/1/organizations").
                queryParam("key", key).
                queryParam("token", token).
                queryParam("displayName", "Org Name").
                body("").
                when().
                post();

        JsonPath path = response.jsonPath();
        organizationId = path.getString("id");
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");
        response.then().body("displayName", Matchers.equalTo("Org Name"));

    }

    @Then("Get Organizations for a member")
    public void getOrganizationsForAMember() throws PendingException {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                basePath("/1/members/me").
                queryParam("key", key).
                queryParam("token", token).
                when().
                get();

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }

    @And("Create a board inside an organization")
    public void createABoardInsideAnOrganization() throws PendingException {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                basePath("/1/boards").
                queryParam("key", key).
                queryParam("token", token).
                queryParam("idOrganization", organizationId).
                queryParam("name", "board Name").
                body("").
                when().
                post();

        JsonPath path = response.jsonPath();
        boardId = path.getString("id");

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }

    @Then("Get boards in an organization")
    public void GetBoardsInAnOrganization() throws PendingException {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                pathParam("organizationId", organizationId).
                queryParam("key", key).
                queryParam("token", token).
                when().
                get("/1/organizations/{organizationId}/boards");


        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }


    @And("Create a new list")
    public void createANewList() throws PendingException {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                basePath("/1/lists").
                queryParam("key", key).
                queryParam("token", token).
                queryParam("idBoard", boardId).
                queryParam("name", "List Name").
                body("").
                when().
                post();

        JsonPath path = response.jsonPath();
        listId = path.getString("id");
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }

    @Then("Get all lists on a board")
    public void getAllListsOnABoard() {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                basePath("/1/boards/" + boardId + "/lists").
                queryParam("key", key).
                queryParam("token", token).
                when().
                get();

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }

    @Then("Archive a list")
    public void archiveAList() {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                queryParam("key", key).
                queryParam("token", token).
                queryParam("value", "true").
                pathParam("id", listId).
                body("").
                when().
                put("/1/lists/{id}/closed");

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");

    }

    @Then("Delete a board")
    public void deleteABoard() {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                queryParam("key", key).
                queryParam("token", token).
                pathParam("boardId", boardId).
                body("").
                when().
                delete("/1/boards/{boardId}");

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");
    }

    @Then("Delete an organization")
    public void deleteAnOrganization() {
        Response response = RestAssured.
                given().
                baseUri(baseURL).
                queryParam("key", key).
                queryParam("token", token).
                pathParam("organizationId",organizationId).
                body("").
                when().
                delete("/1/organizations/{organizationId}");

        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");
    }
}
