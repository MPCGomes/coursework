package bankmanager.service;

public enum FeeStrategy {
    FIXED {
        @Override
        public double apply(double balance) {
            return 10.00;
        }

        @Override
        public String label() {
            return "Fixed (R$ 10.00)";
        }
    },
    PERCENT {
        @Override
        public double apply(double balance) {
            return balance * 0.01;
        }

        @Override
        public String label() {
            return "Percent (1% of balance)";
        }
    },
    NONE {
        @Override
        public double apply(double balance) {
            return 0.0;
        }

        @Override
        public String label() {
            return "None";
        }
    };

    public abstract double apply(double balance);

    public abstract String label();
}
