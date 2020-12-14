package org.pa.balance.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 00000001 => MANUAL
 * 00000010 => GENERATED
 * 00000100 => SUBMITTED
 * 00001000 => ACCEPTED
 */
public class TransactionFlags
{
    private Byte flags = 0;

    public boolean isManual() {
        return (flags & 1) == 1;
    }
    public boolean isGenerated() {
        return ((flags >> 1) & 1) == 1;
    }

    public boolean isSubmitted() {
        return ((flags >> 2) & 1) == 1;
    }

    public boolean isAccepted() {
        return ((flags >> 3) & 1) == 1;
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

        public TransactionFlagsBuilder addSubmitted() {
            this.target.flags = (byte) (this.target.flags + 4);
            return this;
        }

        public TransactionFlagsBuilder addAccepted() {
            this.target.flags = (byte) (this.target.flags + 8);
            return this;
        }

    }
}
