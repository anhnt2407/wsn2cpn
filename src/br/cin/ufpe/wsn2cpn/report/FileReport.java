package br.cin.ufpe.wsn2cpn.report;

import br.cin.ufpe.wsn2cpn.WsnFile;

/**
 *
 * @author avld
 */
public class FileReport extends Report
{

    public FileReport()
    {
        // do nothing
    }

    @Override
    public void make() throws Exception
    {
        WsnFile.save( "topology-result.wsn" , getTopology() );
    }

}
