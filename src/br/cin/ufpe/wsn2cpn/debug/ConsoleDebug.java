package br.cin.ufpe.wsn2cpn.debug;

/**
 *
 * @author avld
 */
public class ConsoleDebug implements Debug
{

    public ConsoleDebug()
    {
        // do nothing
    }
    
    public void print(String msg) 
    {
        System.out.print( msg );
    }

    public void println(String msg)
    {
        System.out.println( msg );
    }

    public void clear()
    {
        System.out.flush();
    }
    
}
