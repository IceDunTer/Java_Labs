package org.boev.lab1;

public class ConverterModel {
    private static final double gram = 1;
    private static final double tableS = 15;
    private static final double teaS = 5;

    public enum Unit {
        gramm("граммы", gram),
        tableSpoon("ст.л", tableS),
        teaSpoon("ч.л", teaS);

        private final String name;
        private final double converter;

        Unit(String name, double converter) {
            this.name = name;
            this.converter = converter;
        }

        public String getName() {
            return name;
        }

        public double getConverter() {
            return converter;
        }
    }

    public double convert(double value, Unit from, Unit to) {
        double grams = value * from.getConverter();
        return grams / to.getConverter();
    }
}
