package packet_readers.lineage2.listeners;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import packet_readers.lineage2.holders.ItemNameHolder;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.*;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.lineage2.L2World;
import packet_readers.lineage2.infos.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:33:17/31.07.2010
 */
public class L2NpcBMListsListener implements IPacketListener
{
	private static final String MULTI_SELL_LIST = "MultiSellList";
	private static final String EX_BUY_SELL_LIST = "ExBuySellListPacket";

	// values
	private static final String LIST_ID = "listId";
	private static final String BUY_LIST = "buyList";
	private static final String ENTRY_ID = "entryId";
	private static final String ENTRY = "entry";

	private Map<Integer, L2MultiSell> _multiSells = new TreeMap<Integer, L2MultiSell>();
	private Map<Integer, L2BuyList> _buyLists = new TreeMap<Integer, L2BuyList>();

	public L2NpcBMListsListener(L2World w)
	{
		//
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p.getName().equalsIgnoreCase(MULTI_SELL_LIST))
		{
			int listId = p.getInt(LIST_ID);
			L2MultiSell multisell = _multiSells.get(listId);
			if (multisell == null)
			{
				multisell = new L2MultiSell(listId);
				_multiSells.put(listId, multisell);
			}

			DataForPart entryList = (DataForPart) p.getRootNode().getPartByName(ENTRY);

			for (DataForBlock entryNode : entryList.getNodes())
			{
				int entryId = ((VisualValuePart) entryNode.getPartByName(ENTRY_ID)).getValueAsInt();
				L2MultiSellEntry entry = new L2MultiSellEntry(entryId);

				DataForPart productions = (DataForPart) entryNode.getPartByName("productions");
				DataForPart ingridients = (DataForPart) entryNode.getPartByName("ingridients");

				for (DataForBlock block : productions.getNodes())
				{
					int itemId = ((VisualValuePart) block.getPartByName("itemId")).getValueAsInt();
					long count = ((VisualValuePart) block.getPartByName("count")).getValueAsLong();

					entry.addProduction(new L2ItemComponent(itemId, count));
				}

				for (DataForBlock block : ingridients.getNodes())
				{
					int itemId = ((VisualValuePart) block.getPartByName("itemId")).getValueAsInt();
					long count = ((VisualValuePart) block.getPartByName("count")).getValueAsLong();

					entry.addIngridient(new L2ItemComponent(itemId, count));
				}

				multisell.addEntry(entry);
			}
		}
		else if (p.getName().equalsIgnoreCase(EX_BUY_SELL_LIST))
		{
			for(DataTreeNode a : p.getRootNode().getNodes())
			{
				if(a instanceof DataSwitchBlock)
				{
					DataSwitchBlock caseBlock = (DataSwitchBlock)a;
					if(caseBlock.getCaseValue() == 0)
					{
						int listId = ((VisualValuePart)caseBlock.getPartByName(LIST_ID)).getValueAsInt();
						L2BuyList buy = _buyLists.get(listId);
						if(buy != null)
						{
							return;
						}
						buy = new L2BuyList(listId);
						_buyLists.put(listId, buy);

						DataForPart buyList = (DataForPart)caseBlock.getPartByName(BUY_LIST);

						for (DataForBlock block : buyList.getNodes())
						{
							DataMacroPart macro = (DataMacroPart)block.getPartByName("item");

							int itemId = ((VisualValuePart) macro.getPartByName("item_id")).getValueAsInt();
							//long count = ((VisualValuePart) macro.getPartByName("count")).getValueAsLong();

							buy.addItem(new L2ItemComponent(itemId, 1));
						}
					}
				}
			}
		}
	}

	@Override
	public void close()
	{
		for (L2MultiSell multisell : _multiSells.values())
		{
			try
			{
				FileWriter writer = new FileWriter("./saves/npc_multisell/" + multisell.getId() + ".xml");
				writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
				writer.write("<list>\n");
				writer.write(String.format("\t<multisell id=\"%d\">\n", multisell.getId()));
				for(L2MultiSellEntry entry : multisell.getEntries())
				{
			   		writer.write(String.format("\t\t<item id=\"%d\">\n", entry.getId()));
					for(L2ItemComponent ingridient : entry.getIngridients())
					{
						writer.write(String.format("\t\t\t<ingredient id=\"%d\" count=\"%d\" /> <!--%s-->\n", ingridient.getItemId(), ingridient.getCount(), ItemNameHolder.getInstance().name(ingridient.getItemId())));
					}

					for(L2ItemComponent production : entry.getProductions())
					{
						writer.write(String.format("\t\t\t<production id=\"%d\" count=\"%d\" /> <!--%s-->\n", production.getItemId(), production.getCount(), ItemNameHolder.getInstance().name(production.getItemId())));
					}
					writer.write("\t\t</item>\n");
				}
				writer.write("</list>");
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		for (L2BuyList buyList : _buyLists.values())
		{
			try
			{
				FileWriter writer = new FileWriter("./saves/npc_buylist/" + buyList.getListId() + ".xml");
				writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
				writer.write("<list>\n");
				writer.write(String.format("\t<tradelist npc=\"%d\" shop=\"1\" markup=\"15\">\n", buyList.getListId()));
				for(L2ItemComponent entry : buyList.getItems())
				{
			   		writer.write(String.format("\t\t<item id=\"%d\" name=\"%s\"/> <!--%s--->\n", entry.getItemId(), entry.getCount(), ItemNameHolder.getInstance().name(entry.getItemId())));
				}
				writer.write("\t</tradelist>\n");
				writer.write("</list>");
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}