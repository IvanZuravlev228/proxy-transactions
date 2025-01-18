package transaction.proxy.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TransactionProxy {
    @Id
    private Long id;
    private String docVob;
    private String docVobName;
    private String docNumber;
    private String docDate;
    private String docVDate;
    private String transDate;
    private String amount;
    private Long amountCop;
    private String currency;
    private String payerEdrpou;
    private String payerName;
    private String payerAccount;
    private String payerMfo;
    private String payerBank;
    private String payerEdrpouFact;
    private String payerNameFact;
    private String reciptEdrpou;
    private String reciptName;
    private String reciptAccount;
    private String reciptMfo;
    private String reciptBank;
    private String reciptEdrpouFact;
    private String reciptNameFact;
    private String paymentDetails;
    private String docAddAttr;
    private String regionId;
    private String paymentType;
    private String paymentData;
    private Integer sourceId;
    private String sourceName;
    private String kekv;
    private String kpk;
    private String contractId;
    private String contractNumber;
    private String budgetCode;
    private String systemKey;
    private String systemKeyFf;
    private String hash;

}
