import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import spock.lang.Specification
import static com.github.tomakehurst.wiremock.client.WireMock.*
import static io.restassured.RestAssured.*

class sampleSpock extends Specification {


    @Rule
    WireMockRule wireMockRule = new WireMockRule()

    def "the pinpong is returned"() {
        given: "the pinpong service is running"

        stubFor(post(urlEqualTo("/pingpong"))
                        .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("PONG")));

        when: "I ping with a rest message"
        def okMessage = "<input>PING</input>"

        given().
                body(okMessage).
                when().
                post("/pingpong").
                then().
                assertThat().
                statusCode(200).
                and().
                assertThat().body(org.hamcrest.Matchers.equalTo("PONG"))

        then: "I receive a ping"

        verify(postRequestedFor(urlMatching("/pingpong"))
                .withRequestBody(matching("<input>PING</input>"))
                .withHeader("Content-Type", notMatching("application/json")));
    }
}
