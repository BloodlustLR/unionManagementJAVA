package org.eu.service.impl;

import org.eu.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {


    @Override
    public String link() {
        return "success";
    }
}
