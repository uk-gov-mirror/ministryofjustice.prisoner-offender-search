package uk.gov.justice.digital.hmpps.prisonersearch.resource

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.prisonersearch.integration.IntegrationTest

class PrisonerSearchResourceTest : IntegrationTest() {

  @Test
  fun `access forbidden when no authority`() {

    webTestClient.get().uri("/prisoner-search/keywords/smith")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `access forbidden when no role`() {

    webTestClient.get().uri("/prisoner-search/keywords/smith")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `can retrieve a list of prisoners with correct role`() {
    webTestClient.get().uri("/prisoner-index/prison/MDI/activeOnly")
      .headers(setAuthorisation(roles = listOf("ROLE_PRISONER_INDEX")))
      .exchange()
      .expectStatus().isOk

    webTestClient.get().uri("/prisoner-search/keywords/smith")
      .headers(setAuthorisation(roles = listOf("ROLE_GLOBAL_SEARCH")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json("{\"content\":[{\"prisonerId\":\"A7089EY\",\"bookingId\":1900836,\"bookingNo\":\"38339B\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":\"1980-12-31\",\"agencyId\":\"MDI\",\"active\":false}],\"pageable\":\"INSTANCE\",\"facets\":[],\"maxScore\":0.2876821,\"totalElements\":1,\"totalPages\":1,\"size\":1,\"numberOfElements\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"number\":0,\"last\":true,\"empty\":false}\n")
  }

}
