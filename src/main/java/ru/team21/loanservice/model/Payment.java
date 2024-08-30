package ru.team21.loanservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Convert(converter = PaymentDetailsConverter.class)
    @Column(columnDefinition = "text")
    private List<PaymentDetails> paymentDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class PaymentDetails {
        private int paymentId;
        private LocalDate paymentDate;
        private BigDecimal paymentAmount;
        private BigDecimal interest;
        private BigDecimal principal;
        private BigDecimal remainingBalance;
        private boolean isPaid;
    }

    @Convert
    public static class PaymentDetailsConverter implements AttributeConverter<List<PaymentDetails>, String> {

        private final ObjectMapper mapper;

        public PaymentDetailsConverter() {
            this.mapper = new ObjectMapper();
            this.mapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String convertToDatabaseColumn(List<PaymentDetails> paymentDetails) {
            try {
                return mapper.writeValueAsString(paymentDetails);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Could not convert PaymentDetails to JSON", e);
            }
        }

        @Override
        public List<PaymentDetails> convertToEntityAttribute(String s) {
            try {
                return mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class,
                        PaymentDetails.class));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Could not convert PaymentDetails to JSON", e);
            }
        }
    }
}
