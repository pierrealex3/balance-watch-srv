package org.pa.balance.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * 00000001 => MANUAL
 * 00000010 => GENERATED
 */
public class TransactionFlags
{
    private Byte flags = 0;

    public static TransactionFlags from(Byte b) {
        return new TransactionFlags(b);
    }

    public boolean isManual() {
        return (flags & 1) == 1;
    }
    public boolean isGenerated() {
        return ((flags >> 1) & 1) == 1;
    }

    public static class TransactionFlagsBuilder {
        private TransactionFlags target = new TransactionFlags();

        public TransactionFlags build() {
            return target;
        }

        public TransactionFlagsBuilder addManual() {
            this.target.flags = (byte) (this.target.flags + 1);
            return this;
        }

        public TransactionFlagsBuilder addGenerated() {
            this.target.flags = (byte) (this.target.flags + 2);
            return this;
        }

    }
}
