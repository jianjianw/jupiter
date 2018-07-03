package com.qiein.jupiter.web.service;

/**
 * 客资领取
 *
 * @author JingChenglong 2018/05/09 17:02
 */
public interface ClientReceiveService {

    /**
     * 客资领取
     *
     * @param kzId
     * @param logId
     * @param companyId
     * @param staffId
     */
    void receive(String kzId, String logId, int companyId, int staffId, String staffName);


    /**
     * 客资领取,pc端，自由领取
     *
     * @param kzId
     * @param companyId
     * @param staffId
     */
    void pcReceive(String kzId, int companyId, int staffId, String staffName);

    /**
     * 客资拒接
     *
     * @param kzId
     * @param logId
     * @param companyId
     * @param staffId
     */
    void refuse(String kzId, String logId, int companyId, int staffId, String staffName);
}