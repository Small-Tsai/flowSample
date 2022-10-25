package com.tsai.flowsample.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface SerialEnum {
    val serialName: String?
}

@Serializable(with = CustomSerializer::class)
enum class Name(override val serialName: String? = null) : SerialEnum {
    Kevin("kevin"),
    Tommy("tommy"),
    Unknown
}

object CustomSerializer : EnumSerializer<Name>(
    enumConstants = Name.values(),
    serialEnumConstants = Name.values().serial(),
    fallback = Name.Unknown
)

open class EnumSerializer<E : Enum<E>>(
    private val enumConstants: Array<E>,
    private val serialEnumConstants: Array<String>,
    private val fallback: E
) : KSerializer<E> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("EnumSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: E) {
        val index = enumConstants.indexOf(value)
        encoder.encodeString(serialEnumConstants[index])
    }

    override fun deserialize(decoder: Decoder): E {
        val string = decoder.decodeString()
        val index = serialEnumConstants.indexOfFirst { it == string }
        return if (index != -1) {
            enumConstants[index]
        } else {
            fallback
        }
    }
}

fun <T> Array<T>.serial() where T : SerialEnum, T : Enum<T> =
    this.map { it.serialName ?: it.name }.toTypedArray()
