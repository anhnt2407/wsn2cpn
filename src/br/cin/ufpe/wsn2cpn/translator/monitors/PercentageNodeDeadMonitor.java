package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author avld
 */
public class PercentageNodeDeadMonitor extends LastNodeDeadMonitor
{
    
    public PercentageNodeDeadMonitor()
    {
        
    }

    @Override
    public Ml getMl()
    {
        int number = getNumber();
        
        if( number == nodeSize )
        {
            return super.getMl();
        }
        else
        {
            return createPercentageMl( number );
        }
    }
    
    private Ml createPercentageMl( int number )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun pred ( bindelem ) = \n" );
        builder.append( "(\n" );
        builder.append( "  " ).append( number ).append( " <= getNodeDead()\n" );
        builder.append( ")" );
        
        return new Ml( builder.toString() );
    }
    
    private int getNumber()
    {
        double n1 = nodeSize * value / 100;
        
        DecimalFormat format = new DecimalFormat( "##" );
        format.setRoundingMode( RoundingMode.UP );
        String n1Str = format.format( n1 );
        
        int nodeNumber = Integer.parseInt( n1Str );
        
        if( nodeNumber == 0 )
        {
            nodeNumber = 1;
        }
        
        return nodeNumber;
    }
    
}
