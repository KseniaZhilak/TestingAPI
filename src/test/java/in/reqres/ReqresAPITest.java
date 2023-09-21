package in.reqres;

import com.beust.ah.A;
import data.*;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;
import static specification.Specification.requestSpec;
import static utils.Util.isSortedList;

public class ReqresAPITest {

    // Тест, без разделения данных от логики теста
    @Owner("Ksenia Zhilak")
    @Test(description = "Создать пользователя 'Ksenia' с ролью 'Learner'")
    public void RequestCreatePost() {
        Map<String,String> requestData = new HashMap<>();
        requestData.put("name","Ksenia");
        requestData.put("job","Learner");
        Response response = given()
                .spec(requestSpec())
                .body(requestData)
                .when()
                .post("api/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        assertEquals(requestData.get("name"), jsonResponse.get("name"),
                "Ожидаемый пользователь: "+requestData.get("name")+ ", Фактический пользователь: "+
                        jsonResponse.get("name"));
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Получить список пользователей")
    public void GetListUsers() {
        Resourse resourse =
            given()
                    .spec(requestSpec())
                    .when()
                    .get("api/users?page=2")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .extract().body().as(Resourse.class);

        assertEquals(resourse.getPage(), 2, "Поле Page");
        assertNotNull(resourse.getPer_page(), "Поле Per_page");
        assertNotNull(resourse.getTotal(),"Поле Total");
        assertNotNull(resourse.getTotal_pages(),"Поле Total_pages");
        assertNotNull(resourse.getData(),"Поле Data");

        resourse.getData().forEach(x -> {
            assertTrue(x.getId() > 0, "Id не должно быть 0");
            assertTrue(x.getAvatar().startsWith("https://"), "Поле Avatar должно начинаться с 'https://'");
        });
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Получить информацию о пользователе")
    public void GetSingleUser() {
        SingleUserResourse resourseSingle =
                given()
                        .spec(requestSpec())
                        .when()
                        .get("api/users/2")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().body().as(SingleUserResourse.class);
        assertEquals(resourseSingle.getData().getId(), 2,
                "Ожидаемый id не получен или пользователя с таким id не существует");
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Получить информацию о несуществующем пользователе")
    public void GetSingleUserNotFound() {
        NotFoundUser notFoundUser =
                given()
                        .spec(requestSpec())
                        .when()
                        .get("api/users/23")
                        .then()
                        .statusCode(404)
                        .log().body()
                        .extract().body().as(NotFoundUser.class);
        Assert.assertTrue(Objects.nonNull(notFoundUser),
                "Объект с ответом равен null");
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Получить список ресурсов")
    public void GetListResource() {
        ListResource listResourse =
                given()
                        .spec(requestSpec())
                        .when()
                        .get("api/unknown")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().body().as(ListResource.class);

        List<Integer> listYear = new ArrayList<>();
        listResourse.getData().forEach(x -> {
            listYear.add(x.getYear());
        });
        assertTrue(isSortedList(listYear), "Данные не отсортированы по годам");
        assertEquals(listResourse.getPage(), 1,
                "Поле page не равно 1");
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Создать пользователя 'Ksenia' с ролью 'Programmer'")
    public void PostCreatePeople() {
        People people = new People("Ksenia", "Programmer");
        PeopleCreated peopleCreated =
                given()
                        .spec(requestSpec())
                        .body(people)
                        .when()
                        .post("api/users")
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().body().as(PeopleCreated.class);
        assertEquals(peopleCreated.getName(), people.getName(),
                "Ожидаемый пользователь: " +people.getName()+", Фактический пользователь: "+peopleCreated.getName());
        assertEquals(peopleCreated.getJob(), people.getJob(),
                "Ожидаемая роль: " +people.getJob()+", Фактическая роль: " +peopleCreated.getJob());
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Обновить данные пользователя или добавить, если его нет")
    public void PutUpdatePeople() {
        People people = new People("Ksenia", "Learner");
        PeopleUpdate peopleUpdate =
                given()
                        .spec(requestSpec())
                        .body(people)
                        .when()
                        .put("api/users/2")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().body().as(PeopleUpdate.class);
        assertEquals(peopleUpdate.getName(), people.getName(),
                "Ожидаемый пользователь: " +people.getName()+", Фактический пользователь: "+peopleUpdate.getName());
        assertEquals(peopleUpdate.getJob(), people.getJob(),
                "Ожидаемая роль: " +people.getJob()+", Фактическая роль: " +peopleUpdate.getJob());
    }

    @Owner("Ksenia Zhilak")
    @Test(description = "Зарегистрировать пользователя")
    public void PostRegisterPeople() {
        PeopleData peopleData = new PeopleData("eve.holt@reqres.in", "pistol");
        RegisterPeople registerPeople =
                given()
                        .spec(requestSpec())
                        .body(peopleData)
                        .when()
                        .post("api/register")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().body().as(RegisterPeople.class);
        assertTrue(registerPeople.getId() > 0, "Id не должен быть 0");
        assertNotNull(registerPeople.getToken(), "Token не должен быть null");
    }
}
/*Запрос на RestAssured начинается с "given"
        Запрос делиться на 3 блока.
        - given и when -> настройка запроса (тело, куки, хедеры)
        - when и then -> отправка запроса (методы, ссылки)
        - после then - > обработка запроса */

