package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.load.CpnLoad;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class ChangeID
{
    private CPN cpn;
    
    private long idInit;
    private boolean processed;
    
    private Map<Long,PageReplace> pageMap;
    
    public ChangeID( long idInitValue , String filename ) throws Exception
    {
        idInit = idInitValue;
        processed = false;
        
        pageMap = new HashMap<Long, PageReplace>();
        
        setCPN( filename );
    }
    
    public ChangeID( long idInitValue , CPN cpn ) throws Exception
    {
        idInit = idInitValue;
        processed = false;
        
        pageMap = new HashMap<Long, PageReplace>();
        
        this.cpn = cpn;
    }
    
    private void setCPN(String filename) throws Exception
    {
        CpnLoad loader = new CpnLoad( filename );
        cpn = loader.getCpn();
    }
    
    public long getLastId()
    {
        return idInit;
    }
    
    public CPN process()
    {
        if( processed )
        {
            return cpn;
        }
        
        processBlock( cpn.getGlobbox() );
        
        //TODO: processGroup
        //TODO: processMonitor
        //TODO: instances
        
        for( Page page : cpn.getPage() )
        {
            processPage( page );
        }
        
        for( Page page : cpn.getPage() )                                     // pega todas as paginas
        {
            for( Trans trans : page.getTrans() )                             // pega todas as transicoes
            {
                processTrans( page , trans );
            }
        }
        
        processed = true;
        return cpn;
    }
    
    private void processBlock( List<BlockItem> blockItemList )
    {
        for( BlockItem item : blockItemList )
        {
            item.setId( ++idInit );
            
            if( item instanceof Block )
            {
                processBlock( ((Block) item).getItemList() );
            }
        }
    }
    
    private void processPage( Page page )
    {
        long idOld = page.getId();          //pegar o ID da pagina antes de mudar
        page.setId( ++idInit );

        for( Arc arc : page.getArcs() )
        {
            processCpnItem( arc );
            processCpnItem( arc.getAnnot() );

            for( CPNItem BendPoint : arc.getBendpointList() )
            {
                processCpnItem( BendPoint );
            }
        }
        
        
        PageReplace pageReplace = new PageReplace( page.getId() );
        
        for( Place place : page.getPlaces() )
        {
            processPlace( page , pageReplace , place );
        }
        
        if( !pageReplace.placeMap.isEmpty() )
        {
            pageMap.put( idOld , pageReplace );
        }
        
        //as transicoes sao tratadas a parte
    }
    
    private void processPlace( Page page , PageReplace pageReplace , Place place )
    {
        long idOld = place.getId();     //pegar o id antigo
        place.setId( ++idInit );        //gera um novo id
        
        processCpnItem( place.getPortType() );  //mudar ID
        processCpnItem( place.getInitmark() );  //mudar ID
        processCpnItem( place.getType() );      //mudar ID

        // mudar ID do place nos Arcos
        for( Arc arc : page.getArcs() )
        {
            if( arc.getPlaceendIdref() == idOld )
            {
                arc.setPlaceendIdref( place.getId() );
            }
        }

        // caso seja uma porta, mudar nas outras paginas (tratado em outro metodo)
        if( place.getPortType() != null )
        {
            pageReplace.placeMap.put( idOld , place.getId() );
        }
    }
    
    private void processTrans( Page page , Trans trans )
    {
        long idOld = trans.getId();     //pegar o id antigo
        trans.setId( ++idInit );        //gera um novo id
        
        processCpnItem( trans.getChannel() );  //mudar ID
        processCpnItem( trans.getCond() );     //mudar ID
        processCpnItem( trans.getCode() );     //mudar ID
        processCpnItem( trans.getPriority() ); //mudar ID
        processCpnItem( trans.getSubpageinfo() ); //mudar ID
        processCpnItem( trans.getTime() );     //mudar ID
        
        // mudar ID do place nos Arcos
        for( Arc arc : page.getArcs() )
        {
            if( arc.getTransendIdref() == idOld )
            {
                arc.setTransendIdref(trans.getId() );
            }
        }
        
        if( trans.getPortSock() == null
                ? false
                : !trans.getPortSock().isEmpty() )                           // a transicao e uma porta
        {
            processTransPortSock( trans );
        }
    }
    
    private void processTransPortSock( Trans trans )
    {
        PageReplace pageReplace = pageMap.get( trans.getPortsockId() );
        
        if( pageReplace == null )
        {
            return ;
        }
        
        for( Entry<Long,Long> entry : pageReplace.placeMap.entrySet() )
        {
            long idOld = entry.getKey();
            long idNew = entry.getValue();
            
            if( trans.getPortSock().containsKey( idOld ) )          // verifica se tem o lugar associado
            {
                List<Long> idList = trans.getPortSock().get( idOld ); // pega o id atual
                trans.getPortSock().remove( idOld );                  // deleta o id anterior
                trans.getPortSock().put( idNew , idList );          // coloca o novo id
            }
        }
        
        trans.setPortsockId( pageReplace.id );
    }
    
    private void processCpnItem( CPNItem item )
    {
        if( item != null )
        {
            item.setId( ++idInit );
        }
    }
    
    // ----------------------------------
    // ---------------------------------- Inner Class
    // ----------------------------------
    
    class PageReplace
    {
        public long id;
        public Map<Long,Long> placeMap;
        
        public PageReplace(long id)
        {
            this.id = id;
            this.placeMap = new HashMap<Long, Long>();
        }
    }
}
