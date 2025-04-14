package com.mooncloak.vpn.component.stargate.message.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntSize
import com.mooncloak.kodetools.compose.serialization.AnnotatedStringSerializer
import com.mooncloak.kodetools.compose.serialization.IntSizeSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.time.Duration

@Serializable
@JsonClassDiscriminator(discriminator = "@type")
@OptIn(ExperimentalSerializationApi::class)
public sealed class Content {

    @SerialName(value = "@context")
    public val context: String = "https://mooncloak.com/types"

    public companion object
}

public sealed class EmbeddedContent : Content()

public sealed class TimelineContent : Content()

public sealed class StateContent : Content()

public sealed class EphemeralContent : Content()


// Embedded Content

@Serializable
@SerialName(value = "EmbeddedMessageContent")
public data class EmbeddedMessageContent public constructor(
    @SerialName(value = "message") public val message: CborWebMessage
) : EmbeddedContent()


// Timeline Content

@Serializable
@SerialName(value = "TextContent")
public class TextContent public constructor(
    @SerialName(value = "annotated") @Serializable(with = AnnotatedStringSerializer::class) public val annotated: AnnotatedString
) : TimelineContent()

/**
 * Represents a URL along with its associated metadata that can be used to display a link card in the UI.
 */

@Serializable
@SerialName(value = "LinkContent")
public data class LinkContent public constructor(
    @SerialName(value = "uri") public val uri: URI,
    @SerialName(value = "title") public val title: String? = null,
    @SerialName(value = "description") @Serializable(with = AnnotatedStringSerializer::class) public val description: AnnotatedString? = null,
    @SerialName(value = "thumbnail") public val thumbnail: ImageContent? = null
) : TimelineContent()

@Serializable
@SerialName(value = "ImageContent")
public data class ImageContent public constructor(
    @SerialName(value = "source") public val source: MediaSource,
    @SerialName(value = "mime_type") public val mimeType: String? = null,
    @SerialName(value = "alt") public val alternative: String? = null,
    @SerialName(value = "byte_size") public val byteSize: Long? = null,
    @SerialName(value = "size") @Serializable(with = IntSizeSerializer::class) public val size: IntSize? = null,
    @SerialName(value = "focal_point") public val focalPoint: FocalPoint? = null,
    @SerialName(value = "blurhash") public val blurHash: BlurHash? = null,
    @SerialName(value = "density") public val density: Float? = null,
    @SerialName(value = "static") public val isStatic: Boolean? = null,
    @SerialName(value = "background") public val backgroundColor: ColorSwatch? = null,
    @SerialName(value = "color_palette") public val colorPalette: ColorPalette? = null
) : TimelineContent()

@Serializable
@SerialName(value = "VideoContent")
public data class VideoContent public constructor(
    @SerialName(value = "source") public val source: MediaSource,
    @SerialName(value = "mime_type") public val mimeType: String? = null,
    @SerialName(value = "alt") public val alternative: String? = null,
    @SerialName(value = "byte_size") public val byteSize: Long? = null,
    @SerialName(value = "size") @Serializable(with = IntSizeSerializer::class) public val size: IntSize? = null,
    @SerialName(value = "frame_rate") public val frameRate: Float? = null,
    @SerialName(value = "bit_rate") public val bitRate: Long? = null,
    @SerialName(value = "codec") public val codec: String? = null,
    @SerialName(value = "start") public val start: Instant? = null,
    @SerialName(value = "end") public val end: Instant? = null,
    @SerialName(value = "live") public val isLive: Boolean = false,
    @SerialName(value = "duration") public val duration: Duration? = null
) : TimelineContent()

@Serializable
@SerialName(value = "AudioContent")
public data class AudioContent public constructor(
    @SerialName(value = "source") public val source: MediaSource,
    @SerialName(value = "mime_type") public val mimeType: String? = null,
    @SerialName(value = "alt") public val alternative: String? = null,
    @SerialName(value = "byte_size") public val byteSize: Long? = null,
    @SerialName(value = "bit_rate") public val bitRate: Long? = null,
    @SerialName(value = "codec") public val codec: String? = null,
    @SerialName(value = "start") public val start: Instant? = null,
    @SerialName(value = "end") public val end: Instant? = null,
    @SerialName(value = "live") public val isLive: Boolean = false,
    @SerialName(value = "duration") public val duration: Duration? = null
) : TimelineContent()

@Serializable
@SerialName(value = "FileContent")
public data class FileContent public constructor(
    @SerialName(value = "source") public val source: MediaSource,
    @SerialName(value = "mime_type") public val mimeType: String? = null,
    @SerialName(value = "alt") public val alternative: String? = null,
    @SerialName(value = "byte_size") public val byteSize: Long? = null
) : TimelineContent()

@Serializable
@SerialName(value = "StickerContent")
public class StickerContent : TimelineContent()

@Serializable
@SerialName(value = "ReactionContent")
public data class ReactionContent public constructor(
    @SerialName(value = "emoji") public val emoji: Emoji
) : TimelineContent()

@Serializable
@SerialName(value = "EventContent")
public class EventContent : TimelineContent()

@Serializable
@SerialName(value = "EventResponseContent")
public class EventResponseContent : TimelineContent()

// TODO: Reminder Actions

@Serializable
@SerialName(value = "LocationContent")
public class LocationContent : TimelineContent()

// TODO: Map? Like Directions

@Serializable
@SerialName(value = "PollContent")
public class PollContent : TimelineContent()

@Serializable
@SerialName(value = "VoteContent")
public class VoteContent : TimelineContent()

// TODO: TODO List Actions

// TODO: Stock/Crypto Tickers

// TODO: Weather

// TODO: Sports Scores

// User status change
@Serializable
@SerialName(value = "StatusContent")
public data class StatusContent public constructor(
    @SerialName(value = "status") public val status: String,
    @SerialName(value = "emoji") public val emoji: Emoji? = null,
    @SerialName(value = "available") public val available: Boolean = false
) : TimelineContent()

// commands ex: /plan_event
public class CommandContent : TimelineContent()

// Ability to remove or delete another message.
@Serializable
@SerialName(value = "RemoveContent")
public data class RemoveContent public constructor(
    @SerialName(value = "target") public val target: URI,
    @SerialName(value = "reason") public val reason: String? = null
) : TimelineContent()


// State Content

@Serializable
@SerialName(value = "RoomNameContent")
public data class RoomNameContent public constructor(
    @SerialName(value = "name") public val name: String
) : StateContent()

@Serializable
@SerialName(value = "PinnedContent")
public data class PinnedContent public constructor(
    @SerialName(value = "pinned") public val pinned: List<URI>
) : StateContent()

@Serializable
@SerialName(value = "TopicContent")
public data class TopicContent public constructor(
    @SerialName(value = "topic") public val topic: String
) : StateContent()


// Ephemeral Content

@Serializable
@SerialName(value = "TypingIndicatorContent")
public data class TypingIndicatorContent public constructor(
    @SerialName(value = "typing") public val typing: Boolean = true
) : EphemeralContent()

@Serializable
@SerialName(value = "ReceiptContent")
public data class ReceiptContent public constructor(
    @SerialName(value = "target") public val target: URI,
    @SerialName(value = "read") public val read: Boolean = false
) : EphemeralContent()

@Serializable
@SerialName(value = "PresenceContent")
public data class PresenceContent public constructor(
    @SerialName(value = "present") public val present: Boolean = true
) : EphemeralContent()