package org.pa.balance.account;

import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAccountRightsPattern
{
    private Integer rightsPattern = 0;

    public static UserAccountRightsPattern from(Integer rightsPattern) {
        return new UserAccountRightsPattern(Optional.ofNullable(rightsPattern).orElse(0));
    }

    public boolean isOwner() {
        return (rightsPattern.intValue() & 1) == 1;
    }

    public boolean isAdmin() {
        return ((rightsPattern.intValue() >> 1) & 1) == 1;
    }

    public boolean isTransfer() {
        return ((rightsPattern.intValue() >> 2) & 1) == 1;
    }

    public boolean isRead() { return ((rightsPattern.intValue() >> 3) & 1) == 1; }

    public static class UserAccountRightsPatternBuilder {

        private UserAccountRightsPattern target = new UserAccountRightsPattern();

        public UserAccountRightsPattern build() {
            return target;
        }

        public UserAccountRightsPatternBuilder addOwner() {
            this.target.rightsPattern += 1;
            return this;
        }

        public UserAccountRightsPatternBuilder addAdmin() {
            this.target.rightsPattern += 2;
            return this;
        }

        public UserAccountRightsPatternBuilder addTransfer() {
            this.target.rightsPattern += 4;
            return this;
        }

        public UserAccountRightsPatternBuilder addRead() {
            this.target.rightsPattern += 8;
            return this;
        }

        /**
         * All the existing rights should be assigned!
         */
        public UserAccountRightsPatternBuilder addVip() {
            addOwner().addAdmin().addTransfer().addRead();
            return this;
        }

    }
}
