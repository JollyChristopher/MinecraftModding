package org.halemaster.flexistats;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;

import java.util.Iterator;
import java.util.Random;

public class RandomEvaluator extends DoubleEvaluator
{
    public static final Operator RANDOM = new Operator("d", 2, Operator.Associativity.LEFT, 4);
    private static final Parameters PARAMS;

    static {
        // Gets the default DoubleEvaluator's parameters
        PARAMS = DoubleEvaluator.getDefaultParameters();
        // add the new sqrt function to these parameters
        PARAMS.add(RANDOM);
    }

    public RandomEvaluator() {
        super(PARAMS);
    }

    private Random random = new Random();

    public Double getRandom(Iterator<Double> operands)
    {
        Integer value = 0;
        int amount = operands.next().intValue();
        int type = operands.next().intValue();

        for(int i = 0; i < amount; i++)
        {
            value += random.nextInt(type) + 1;
        }

        return value.doubleValue();
    }

    @Override
    protected Double evaluate(Operator operator, Iterator<Double> operands, Object evaluationContext) {
        if (operator == RANDOM)
        {
            return getRandom(operands);
        }
        else
        {
            return super.evaluate(operator, operands, evaluationContext);
        }
    }
}
