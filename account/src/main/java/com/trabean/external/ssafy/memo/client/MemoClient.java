package com.trabean.external.ssafy.memo.client;

import com.trabean.config.FeignClientConfig;
import com.trabean.external.ssafy.memo.dto.request.TransactionMemoRequestDTO;
import com.trabean.external.ssafy.memo.dto.response.TransactionMemoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SSAFY 금융 API p.266 ~ p.268 거래내역 메모 관련 요청 처리 클라이언트
 */
@FeignClient(name = "memoClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu", configuration = FeignClientConfig.class)

public interface MemoClient {

    @PostMapping("/transactionMemo")
    TransactionMemoResponseDTO transactionMeno(@RequestBody TransactionMemoRequestDTO requestDTO);
}
