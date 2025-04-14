package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
public value class Relation public constructor(
    public val value: String
) {

    public companion object {

        public val Quote: Relation = Relation(value = "quote")
        public val Mention: Relation = Relation(value = "mention")
        public val Tag: Relation = Relation(value = "tag")
    }
}
