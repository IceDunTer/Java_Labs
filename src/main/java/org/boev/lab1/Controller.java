package org.boev.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label result;
    @FXML
    private RadioButton teaBtn;
    @FXML
    private RadioButton tableBtn;
    @FXML
    private TextField weight;
    @FXML
    private TextField amount;
    private ConverterModel model;

    @FXML
    public void initialize() {
        model = new ConverterModel();
    }

    public void convertClicked() {
        try {
            String weightText = weight.getText().trim();
            String amountText = amount.getText().trim();

            if (weightText.isEmpty() || amountText.isEmpty()) {
                result.setText("Заполните все поля!");
                return;
            }
            double weightValue = Double.parseDouble(weightText.replace(',', '.'));
            double amountValue = Double.parseDouble(amountText.replace(',', '.'));

            if (weightValue <= 0 || amountValue <= 0) {
                result.setText("Значения должны быть больше 0!");
                return;
            }

            double totalGrams = weightValue * amountValue;
            ConverterModel.Unit selectedUnit;
            if (teaBtn.isSelected()) {
                selectedUnit = ConverterModel.Unit.teaSpoon;
            } else {
                selectedUnit = ConverterModel.Unit.tableSpoon;
            }

            double convertedValue = model.convert(totalGrams, ConverterModel.Unit.gramm, selectedUnit);
            result.setText(String.format("Результат: %.2f %s", convertedValue, selectedUnit.getName()));

        } catch (NumberFormatException e) {
            result.setText("Введите корректные числа!");
        } catch (Exception e) {
            result.setText("Ошибка конвертации!");
        }
    }
}
