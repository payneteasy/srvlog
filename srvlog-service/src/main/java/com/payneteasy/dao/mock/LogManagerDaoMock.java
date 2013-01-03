package com.payneteasy.dao.mock;

import com.payneteasy.dao.ILogManagerDao;
import com.payneteasy.service.ILogManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 03.01.13
 */
@Service
public class LogManagerDaoMock implements ILogManagerDao{
    @Override
    public void saveLogMessage(String message) {
        System.out.println("save message = "+message);
        logs.add(message);
    }

    @Override
    public List<String> getLogs() {
        return logs;
    }

    List<String> logs = new ArrayList<String>();
}
