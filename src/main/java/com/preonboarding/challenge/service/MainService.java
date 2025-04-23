package com.preonboarding.challenge.service;

import com.preonboarding.challenge.service.dto.MainPageDto;

public interface MainService {
    MainPageDto.MainPageResponse getMainPageContents();
}