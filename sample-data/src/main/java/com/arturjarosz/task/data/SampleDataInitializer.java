package com.arturjarosz.task.data;

import com.arturjarosz.task.sharedkernel.annotations.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@ApplicationService
public class SampleDataInitializer extends AbstractDataInitializer {

    private static final Logger LOG = LogManager.getLogger(SampleDataInitializer.class);
    private final List<DataInitializer> dataInitializers;
    private final TransactionHandler transactionHandler;

    @Autowired
    public SampleDataInitializer(List<DataInitializer> dataInitializers, TransactionHandler transactionHandler) {
        this.dataInitializers = dataInitializers;
        this.transactionHandler = transactionHandler;
    }

    /**
     * Loading all sample data.
     */

    @Override
    protected void loadData() {
        LOG.info("Loading sample data.");

        for (DataInitializer dataInitializer : this.dataInitializers) {
            try {
                this.transactionHandler.runInTransaction(transactionStatus -> {
                    dataInitializer.initializeData();
                    return null;
                });
            } catch (Exception e) {
                throw new RuntimeException(":(");
            }
        }

        LOG.info("All sample data loaded.");
    }
}
