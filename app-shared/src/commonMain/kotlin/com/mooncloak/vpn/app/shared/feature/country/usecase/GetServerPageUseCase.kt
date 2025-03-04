package com.mooncloak.vpn.app.shared.feature.country.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.LocationCode
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.OffsetDecodedCursor
import com.mooncloak.kodetools.pagex.PageInfo
import com.mooncloak.kodetools.pagex.ResolvedPage
import com.mooncloak.kodetools.pagex.decode
import com.mooncloak.kodetools.pagex.encode
import com.mooncloak.kodetools.pagex.invoke
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.server.ServerRepository
import kotlinx.serialization.json.Json

@OptIn(ExperimentalPaginationAPI::class)
public class GetServerPageUseCase @Inject public constructor(
    private val serverRepository: ServerRepository,
    private val json: Json
) {

    public suspend operator fun invoke(
        locationCode: LocationCode,
        cursor: Cursor? = null,
        direction: Direction = Direction.After,
        count: Int = 20
    ): ResolvedPage<Server> {
        val decodedCursor = cursor?.decode(
            format = json,
            deserializer = OffsetDecodedCursor.serializer()
        )
        val offset = if (direction == Direction.After) {
            (decodedCursor?.offset?.toInt() ?: 0).coerceAtLeast(0)
        } else {
            (decodedCursor?.let { it.offset.toInt() - it.count.toInt() - count } ?: 0).coerceAtLeast(0)
        }

        val servers = serverRepository.get(
            filters = ServerFilters(
                locationCode = locationCode
            ),
            count = count,
            offset = offset
        )

        val firstCursor = Cursor.encode(
            offset = offset.toUInt(),
            count = count.toUInt()
        )
        val lastCursor = Cursor.encode(
            offset = (offset + servers.size).toUInt(),
            count = count.toUInt()
        )

        return ResolvedPage(
            items = servers,
            info = PageInfo(
                firstCursor = firstCursor,
                lastCursor = lastCursor
            )
        )
    }
}
