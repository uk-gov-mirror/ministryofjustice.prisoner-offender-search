package uk.gov.justice.digital.hmpps.prisonersearch.services.dto

import java.time.LocalDate

data class OffenderBooking (
  val offenderNo: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
  val activeFlag : Boolean,
  val bookingId: Long? = null,
  val bookingNo: String? = null,
  val middleName: String? = null,
  val aliases: List<Alias>? = null,
  val agencyId: String? = null,
  val inOutStatus: String? = null,
  val religion: String? = null,
  val language: String? = null,
  val alerts: List<Alert>? = null,
  val assignedLivingUnit: AssignedLivingUnit? = null,
  val facialImageId: Long? = null,
  val age: Int? = null,
  val physicalAttributes: PhysicalAttributes? = null,
  val physicalCharacteristics: List<PhysicalCharacteristic>? = null,
  val profileInformation: List<ProfileInformation>? = null,
  val physicalMarks: List<PhysicalMark>? = null,
  val assessments: List<Assessment>? = null,
  val csra: String? = null,
  val categoryCode: String? = null,
  val birthPlace: String? = null,
  val birthCountryCode: String? = null,
  val identifiers: List<OffenderIdentifier>? = null,
  val sentenceDetail: SentenceDetail? = null,
  val offenceHistory: List<OffenceHistoryDetail>? = null,
  val status: String? = null,
  val legalStatus: String? = null,
  val imprisonmentStatus: String? = null,
  val personalCareNeeds : List<PersonalCareNeed>? = null
)