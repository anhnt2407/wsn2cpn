package br.cin.ufpe.wsn2cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNXML;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.load.CpnLoad;
import br.cin.ufpe.wsn2cpn.debug.ConsoleDebug;
import br.cin.ufpe.wsn2cpn.execute.AccessCpnSingleExecute;
import java.io.FileWriter;

/**
 *
 * @author avld
 */
public class Wsn2CpnTesteMain 
{
 
    public static Page getPage()
    {
        Place place1 = new Place( "start" , new CPNItem( "INT" ) , new CPNItem( "0" ) );
        place1.setPosattrX( 0 );
        place1.setPosattrY( 0 );
        
        Place place2 = new Place( "end"   , new CPNItem( "INT" ) , null );
        place2.setPosattrX( 150 );
        place2.setPosattrY( 0  );
        
        Trans trans = new Trans( "process" );
        trans.setPosattrX( 70 );
        trans.setPosattrY( 0  );
        
        Arc arc1 = new Arc( "i" );
        arc1.setAnnot( new CPNItem( "i" ) );
        arc1.setOrientation( Arc.PlaceToTransition );
        arc1.setPlaceendIdref( place1.getId() );
        arc1.setTransendIdref( trans.getId()  );
        
        Arc arc2 = new Arc();
        arc2.setAnnot( new CPNItem( "i" ) );
        arc2.setOrientation( Arc.TransitionToPlace );
        arc2.setPlaceendIdref( place2.getId() );
        arc2.setTransendIdref( trans.getId()  );
        
        Page page = new Page();
        page.setPageattrName( "Network" );
        
        page.getArcs().add( arc1 );
        page.getArcs().add( arc2 );
        
        page.getTrans().add( trans );
        
        page.getPlaces().add( place1 );
        page.getPlaces().add( place2 );
        
        return page;
    }
    
    public static void main(String arg[]) throws Exception
    {
        //String filename = "./layers/mac/perfect_mac.cpn";
        String filename = "/home/avld/NetBeansProjects/Doutorado/NodeDeployAutomatic/wsn_model_teste.cpn";
        String model_file = "./wsn_model_2.cpn";
        
        // ---------------------------------- //
        
        CpnLoad loader = new CpnLoad( filename );
        CPN cpn = loader.getCpn();
        
//        Page page = getPage();
//        
//        cpn.getPage().clear();
//        cpn.getPage().add( page );
//        
//        cpn.getInstances().clear();
//        cpn.getInstances().add( new InstanceItens( page.getId() ) );
//        
//        cpn.setMonitors( new MonitorBlock() );
        
        System.out.println( "Convertendo o objeto em XML..." );
        String modelStr = CPNXML.convertTo( cpn );
        
        // ---------------------------------- //
        
        System.out.println("criando o arquivo... " + model_file );

        FileWriter writer = new FileWriter( model_file );
        writer.write( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n\n" );
        writer.write( modelStr );
        writer.close();
        
        // ---------------------- //
        
        String filename_2 = "./wsn_model.cpn";
        //String teste = "/home/avld/NetBeansProjects/Doutorado/NodeDeployAutomatic/wsn_model_teste.cpn";
        AccessCpnSingleExecute executor = new AccessCpnSingleExecute( filename_2 , new ConsoleDebug() );
        executor.executar();
    }
    
}
