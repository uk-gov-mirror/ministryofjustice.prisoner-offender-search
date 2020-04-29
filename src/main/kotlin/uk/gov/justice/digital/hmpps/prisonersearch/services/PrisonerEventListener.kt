package uk.gov.justice.digital.hmpps.prisonersearch.services

import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service


@Service
open class PrisonerEventListener(
    private val prisonerSyncService: PrisonerSyncService,
    @Qualifier("gson") private val gson : Gson
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @JmsListener(destination = "\${sqs.queue.name}", containerFactory = "jmsListenerContainerFactory")
  fun processOffenderEvent(requestJson: String?) {
    log.debug(requestJson)
    val (message, messageId, messageAttributes) = gson.fromJson(requestJson, Message::class.java)
    val eventType = messageAttributes.eventType.Value
    log.debug("Received message {} type {}", messageId, eventType)

    when (eventType) {
      "EXTERNAL_MOVEMENT_RECORD-INSERTED" -> prisonerSyncService.externalMovement(fromJson(message))
      "BED_ASSIGNMENT_HISTORY-INSERTED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "IMPRISONMENT_STATUS-CHANGED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "SENTENCE_DATES-CHANGED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "ASSESSMENT-CHANGED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "OFFENDER_BOOKING-REASSIGNED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "OFFENDER_BOOKING-CHANGED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "BOOKING_NUMBER-CHANGED" -> prisonerSyncService.offenderBookingChange(fromJson(message))
      "OFFENDER_DETAILS-CHANGED" -> prisonerSyncService.offenderChange(fromJson(message))
      "OFFENDER-UPDATED" -> prisonerSyncService.offenderChange(fromJson(message))

      else -> log.warn("We received a message of event type {} which I really wasn't expecting", eventType)
    }

  }

  private inline fun <reified T> fromJson(message: String): T {
    return gson.fromJson(message, T::class.java)
  }
}

data class EventType(val Value: String)
data class MessageAttributes(val eventType: EventType)
data class Message(val Message: String, val MessageId: String, val MessageAttributes: MessageAttributes)

data class ExternalPrisonerMovementMessage(val bookingId: Long,
                                           val movementSeq: Long,
                                           val offenderIdDisplay: String,
                                           val fromAgencyLocationId: String,
                                           val toAgencyLocationId: String,
                                           val directionCode: String,
                                           val movementType: String)

data class OffenderBookingChangedMessage(val bookingId: Long)

data class OffenderChangedMessage(val offenderIdDisplay: String)