package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Topology;

/**
 *
 * @author avld
 */
public class GridNodeDeploy extends NodeDeploy
{

    public GridNodeDeploy()
    {
        this.name = "Grid";
    }
    
    @Override
    public Topology create( Topology topology ) throws Exception
    {
        int cellSize = (width * heigth) / nodeSize ;
        
        return topology;
    }
    
}
