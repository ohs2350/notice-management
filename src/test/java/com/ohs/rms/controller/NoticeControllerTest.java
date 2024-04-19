package com.ohs.rms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
import com.ohs.rms.service.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeService noticeService;

    private final ObjectMapper objectMapper;


    public NoticeControllerTest() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("정상적인 입력값으로 생성 요청 시 201코드를 반환한다.")
    void create() throws Exception {
        // given
        NoticeCreateRequest noticeCreateRequest = new NoticeCreateRequest("test", "test", LocalDateTime.now(), LocalDateTime.now(), null);
        given(noticeService.create(any(NoticeCreateRequest.class))).willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeCreateRequest))
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", is("/notice/" + 1L)))
                .andDo(print());
        verify(noticeService).create(any(NoticeCreateRequest.class));
    }

    @Test
    @DisplayName("비정상적인 입력값으로 생성 요청 시 400코드를 반환한다.")
    void createWithInvalidRequest() throws Exception {
        // given
        NoticeCreateRequest noticeCreateRequest = new NoticeCreateRequest(null, null, null, null, null);
        given(noticeService.create(any(NoticeCreateRequest.class))).willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeCreateRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
        verify(noticeService, never()).create(any(NoticeCreateRequest.class));
    }

    @Test
    @DisplayName("정상적인 입력값으로 조회 시 공지사항을 반환한다.")
    void read() throws Exception {
        // given
        given(noticeService.read(anyLong())).willReturn(any(NoticeReadResponse.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/notice/{noticeId}", 1)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
        verify(noticeService).read(anyLong());
    }

    @Test
    @DisplayName("잘못된 id로 조회 시 요청 실패 및 404코드를 반환한다.")
    void readWithInvalidNoticeId() throws Exception {
        // given
        given(noticeService.read(anyLong())).willThrow(new NoSuchElementException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/notice/{noticeId}", 1)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("정상적인 입력값으로 수정 요청 시 200코드를 반환한다.")
    void update() throws Exception {
        // given
        NoticeUpdateRequest request = new NoticeUpdateRequest("test", "test", LocalDateTime.now(), LocalDateTime.now());
        willDoNothing().given(noticeService).update(anyLong(), any(NoticeUpdateRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/notice/{noticeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
        verify(noticeService).update(anyLong(), any(NoticeUpdateRequest.class));
    }

    @Test
    @DisplayName("비정상적인 입력값으로 수정 요청 시 400코드를 반환한다.")
    void updateWithInvalidRequest() throws Exception {
        // given
        NoticeUpdateRequest request = new NoticeUpdateRequest(null, null, null, null);
        willDoNothing().given(noticeService).update(anyLong(), any(NoticeUpdateRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/notice/{noticeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
        verify(noticeService, never()).update(anyLong(), any(NoticeUpdateRequest.class));
    }

    @Test
    @DisplayName("잘못된 id로 수정 요청 시 실패 및 404코드를 반환한다.")
    void updateWithInvalidNoticeId() throws Exception {
        // given
        NoticeUpdateRequest request = new NoticeUpdateRequest("test", "test", LocalDateTime.now(), LocalDateTime.now());
        willThrow(new NoSuchElementException()).given(noticeService).update(anyLong(), any(NoticeUpdateRequest.class));


        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/notice/{noticeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("정상적인 입력값으로 삭제 요청 시 200코드를 반환한다.")
    void delete() throws Exception {
        // given
        willDoNothing().given(noticeService).delete(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/notice/{noticeId}", 1)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
        verify(noticeService).delete(anyLong());
    }

    @Test
    @DisplayName("잘못된 id로 삭제 요청 시 실패 및 404코드를 반환한다.")
    void deleteWithInvalidNoticeId() throws Exception {
        // given
        willThrow(new NoSuchElementException()).given(noticeService).delete(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/notice/{noticeId}", 1)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }
}