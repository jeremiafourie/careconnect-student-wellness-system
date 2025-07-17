package com.bc.desktop.controllers;

import com.bc.desktop.models.Counselor;
import com.bc.desktop.services.DatabaseService;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class CounselorController {
    private final DatabaseService dbService;

    public CounselorController(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public void getAllCounselors(Consumer<List<Counselor>> onSuccess, Consumer<Exception> onError) {
        SwingWorker<List<Counselor>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Counselor> doInBackground() {
                return dbService.getAllCounselors();
            }

            @Override
            protected void done() {
                try {
                    List<Counselor> counselors = get();
                    onSuccess.accept(counselors);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        };
        worker.execute();
    }

    public void addCounselor(Counselor counselor, Consumer<Boolean> onSuccess, Consumer<Exception> onError) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return dbService.saveCounselor(counselor);
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    onSuccess.accept(success);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        };
        worker.execute();
    }

    public void updateCounselor(Counselor counselor, Consumer<Boolean> onSuccess, Consumer<Exception> onError) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return dbService.updateCounselor(counselor);
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    onSuccess.accept(success);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        };
        worker.execute();
    }

    public void deleteCounselor(Long counselorId, Consumer<Boolean> onSuccess, Consumer<Exception> onError) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return dbService.deleteCounselor(counselorId);
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    onSuccess.accept(success);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        };
        worker.execute();
    }
}