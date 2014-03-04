package br.cin.ufpe.wsn2cpn;

/**
 *
 * @author avld
 */
public class WsnFile
{

    public static void save(String filename, Topology topology) throws Exception
    {
        WsnFileSave saver = new WsnFileSave();
        saver.process( topology );
        saver.save( filename );

        saver = null;
    }

    public static Topology open(String filename) throws Exception
    {
        WsnFileOpen opener = new WsnFileOpen();
        opener.open( filename );
        return opener.getTopology();
    }

}
