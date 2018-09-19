package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.SearchClientVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: shiTao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebClientInfoSearchDaoTest {

    @Autowired
    private QueryClientByKeyDao webClientInfoSearchDao;

    @Test
    public void searchClientInfoBySearchKey() {
        List<SearchClientVO> searchClientVOS = webClientInfoSearchDao.search(3, "66WJWH");
        System.out.println(searchClientVOS.size());
    }
}