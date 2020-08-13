package org.pa.balance.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
public class PatternWrapper
{
    private Pattern level1Pattern;
    private Pattern level2Pattern;
}
