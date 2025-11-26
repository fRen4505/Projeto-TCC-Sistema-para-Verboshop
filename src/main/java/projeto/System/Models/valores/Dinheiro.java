package projeto.System.Models.valores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Dinheiro {
    
    private Currency real = Currency.getInstance("BRL");
    private BigDecimal quantia;

    public Dinheiro(Double insQuantia) throws IllegalArgumentException{
        if (insQuantia > 0) {
            BigDecimal tempVal = new BigDecimal(insQuantia);
            this.quantia = tempVal.setScale(real.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
        }else {
            this.quantia = null;
            throw new IllegalArgumentException("Quantia inserida é invalida");
        }
    }

    public void novaQuantia(double insQuantia) throws IllegalArgumentException{
        if (insQuantia >= 0) {
            BigDecimal tempVal = new BigDecimal(insQuantia);
            this.quantia = tempVal.setScale(real.getDefaultFractionDigits());
        }else {
            this.quantia = null;
            throw new IllegalArgumentException("Quantia inserida é invalida");
        }
    }

    public String valor(){
        return real.getSymbol() + " " + quantia.toString();
    }
    
    public BigDecimal getQuantia() {
        return quantia;
    }

    public Double getQuantiaDouble(){
        return quantia.doubleValue();
    }


}
