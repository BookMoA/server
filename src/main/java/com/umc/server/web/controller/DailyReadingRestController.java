package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.DailyReadingConverter;
import com.umc.server.domain.DailyReading;
import com.umc.server.domain.Member;
import com.umc.server.service.DailyReading.DailyReadingService;
import com.umc.server.web.dto.response.DailyReadingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memberBooks")
public class DailyReadingRestController {

    private final DailyReadingService dailyReadingService;

    @Operation(summary = "하루 독서량 생성 API", description = "멤버 책 생성 시 하루 독서량이 생성되는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 하루 독서량 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/dailyReading")
    public ApiResponse<DailyReadingResponseDTO.CreateDailyReadingResultDTO> createDailyReading(
            @RequestParam(name = "memberBookId") Long memberBookId) {
        DailyReading dailyReading = dailyReadingService.createDailyReading(memberBookId);
        return ApiResponse.onSuccess(
                DailyReadingConverter.toCreateDailyReadingResultDTO(dailyReading));
    }

    @Operation(summary = "하루 독서량 조회 API", description = "특정 날짜, 특정 멤버 책의 하루 독서량을 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 하루 독서량 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(
                name = "memberBookId",
                description = "하루 독서량을 찾고 싶은 멤버 책의 id, query parameter 입니다."),
        @Parameter(
                name = "createdAt",
                description = "특정 책의 하루 독서량이 기록된 날짜(e.g. 2024-01-01), query parameter 입니다.")
    })
    @GetMapping("/dailyReading")
    public ApiResponse<DailyReadingResponseDTO.DailyReadingPreviewDTO> readDailyReading(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam(name = "memberBookId") Long memberBookId,
            @RequestParam(name = "createdAt") LocalDate createdAt) {
        DailyReading dailyReading =
                dailyReadingService.readDailyReading(signInmember, memberBookId, createdAt);
        return ApiResponse.onSuccess(DailyReadingConverter.toDailyReadingPreviewDTO(dailyReading));
    }

    @Operation(summary = "하루 독서량 전체 조회(독서통장) API", description = "독서 통장에서 전체 하루 독서량을 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 하루 독서량 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/readingBank")
    public ApiResponse<DailyReadingResponseDTO.DailyReadingPreviewListDTO> dailyReadingPage(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        List<DailyReading> dailyReadingList =
                dailyReadingService.readDailyReadingList(signInmember);
        return ApiResponse.onSuccess(
                DailyReadingConverter.toDailyReadingPreviewListDTO(dailyReadingList));
    }

    @Operation(
            summary = "하루 독서량 수정 API",
            description = "(하루에 같은 책을 두 번 이상 읽을 시 하루 독서량 수정) 당일 생성된 하루 독서량을 수정하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 하루 독서량 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(
                name = "memberBookId",
                description = "하루 독서량을 찾고 싶은 멤버 책의 id, query parameter 입니다."),
    })
    @PatchMapping("/dailyReading")
    public ApiResponse<DailyReadingResponseDTO.DailyReadingPreviewDTO> updateDailyReading(
            @RequestParam(name = "memberBookId") Long memberBookId) {
        DailyReading dailyReading = dailyReadingService.updateDailyReading(memberBookId);
        return ApiResponse.onSuccess(DailyReadingConverter.toDailyReadingPreviewDTO(dailyReading));
    }
}
