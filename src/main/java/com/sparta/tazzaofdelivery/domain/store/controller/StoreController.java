package com.sparta.tazzaofdelivery.domain.store.controller;


import com.sparta.tazzaofdelivery.config.annotation.Auth;
import com.sparta.tazzaofdelivery.domain.search.service.SearchService;
import com.sparta.tazzaofdelivery.domain.store.dto.request.StoreCreateRequest;
import com.sparta.tazzaofdelivery.domain.store.dto.response.*;
import com.sparta.tazzaofdelivery.domain.store.enums.StoreStatus;
import com.sparta.tazzaofdelivery.domain.store.service.StoreService;
import com.sparta.tazzaofdelivery.domain.user.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<StoreCreateResponse> createStore(@RequestBody StoreCreateRequest request, @Auth AuthUser authUser) {
        StoreCreateResponse response = storeService.createStore(request, authUser);
        System.out.println("::: authUser.getId() = " + authUser.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StoreGetAllResponse>> getAllStores(){
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreGetResponse> getStoreAndMenu(@PathVariable Long storeId){
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId, @Auth AuthUser user){
      storeService.deleteStore(storeId,user);
      return ResponseEntity.ok("가게가 정상 폐업 처리 되었습니다. ");
    }

    // 통합검색
    @GetMapping("/search")
    public ResponseEntity<List<StoreIntegratedResponse>> searchStores(@RequestParam(required = false) String storeName,
                                                                      @RequestParam(required = false) String menuName,
                                                                      @RequestParam(required = false) StoreStatus status){
        List<StoreIntegratedResponse> results = storeService.searchStores(storeName, menuName, status);

        if(storeName != null){
            searchService.recordSearchKeyword(storeName);
        }

        return ResponseEntity.ok(results);
    }
}
