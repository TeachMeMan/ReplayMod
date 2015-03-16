package eu.crushedpixel.replaymod.gui.replaystudio;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.apache.commons.io.FilenameUtils;

import eu.crushedpixel.replaymod.gui.GuiConstants;
import eu.crushedpixel.replaymod.gui.elements.GuiArrowButton;
import eu.crushedpixel.replaymod.gui.elements.GuiDropdown;
import eu.crushedpixel.replaymod.gui.elements.GuiEntryList;
import eu.crushedpixel.replaymod.gui.elements.listeners.SelectionListener;
import eu.crushedpixel.replaymod.registry.ReplayGuiRegistry;
import eu.crushedpixel.replaymod.utils.ReplayFileIO;

public class GuiConnectPart extends GuiStudioPart {

	private static final String DESCRIPTION = "Connects multiple Replays in the same order as the list.";
	private static final String TITLE = "Connect Replays";

	private boolean initialized = false;
	
	private GuiEntryList<String> concatList;
	private GuiDropdown<String> replayDropdown;
	
	private GuiButton removeButton, addButton;
	private GuiArrowButton upButton, downButton;
	
	private List<File> replayFiles;
	private List<String> filesToConcat;

	public GuiConnectPart(int yPos) {
		super(yPos);
		this.mc = Minecraft.getMinecraft();
		fontRendererObj = mc.fontRendererObj;
	}

	@Override
	public void applyFilters() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void initGui() {
		if(!initialized) {
			concatList = new GuiEntryList(1, fontRendererObj, 30, yPos, 150, 0);
			filesToConcat = new ArrayList<String>();
			String selectedName = FilenameUtils.getBaseName(GuiReplayStudio.instance.getSelectedFile().getAbsolutePath());
			filesToConcat.add(selectedName);
			concatList.setElements(filesToConcat);

			concatList.setSelectionIndex(0);
			
			replayDropdown = new GuiDropdown(1, fontRendererObj, 250, yPos+5, 0, 4);
			
			replayDropdown.clearElements();
			replayFiles = ReplayFileIO.getAllReplayFiles();
			int index = -1;
			int i=0;
			for(File file : replayFiles) {
				String name = FilenameUtils.getBaseName(file.getAbsolutePath());
				replayDropdown.addElement(name);
				if(name.equals(selectedName)) {
					index = i;
				}
				i++;
			}
			
			replayDropdown.setSelectionPos(index);
			
			replayDropdown.addSelectionListener(new SelectionListener() {
				
				@Override
				public void onSelectionChanged(int selectionIndex) {
					filesToConcat.set(concatList.getSelectionIndex(), (String)replayDropdown.getElement(selectionIndex));
					concatList.setElements(filesToConcat);
					
				}
			});
			
			concatList.addSelectionListener(new SelectionListener() {	
				@Override
				public void onSelectionChanged(int selectionIndex) {
					String selName = (String)concatList.getElement(selectionIndex);
					int i = 0;
					for(Object s : replayDropdown.getAllElements()) {
						String str = (String)s;
						if(str.equals(selName)) {
							replayDropdown.setSelectionIndex(i);
							break;
						}
						i++;
					}
				}
			});
			
			upButton = new GuiArrowButton(GuiConstants.REPLAY_EDITOR_UP_BUTTON, 195, yPos+40, "", true);
			buttonList.add(upButton);
			
			downButton = new GuiArrowButton(GuiConstants.REPLAY_EDITOR_DOWN_BUTTON, 219, yPos+40, "", false);
			buttonList.add(downButton);
			
			int w = GuiReplayStudio.instance.width-243-20-4;
			
			removeButton = new GuiButton(1, 243, yPos+40, "Remove");
			buttonList.add(removeButton);
			
			addButton = new GuiButton(1, 0, yPos+40, "Add");
			buttonList.add(addButton);
			
		}
		
		int w = GuiReplayStudio.instance.width-243-20-4;
		addButton.xPosition = 243+6+(w/2);
		
		addButton.width = w/2+2;
		removeButton.width = w/2+2;
		
		replayDropdown.width = GuiReplayStudio.instance.width-250-20;
		
		int h = GuiReplayStudio.instance.height-yPos-20;
		int rows = (int)(h / (float)GuiEntryList.elementHeight);
		concatList.setVisibleElements(rows);
		
		initialized = true;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		concatList.mouseClicked(mouseX, mouseY, mouseButton);
		replayDropdown.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		concatList.drawTextBox();
		replayDropdown.drawTextBox();
		
		drawString(fontRendererObj, "Replay:", 200, yPos+5+7, Color.WHITE.getRGB());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		if(!initialized) initGui();
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

	}
}
