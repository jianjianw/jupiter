package com.qiein.jupiter.web.service;

import com.github.pagehelper.Page;

import java.util.List;

public interface StaffService<T> {

    public Page<T> get();
}
