package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.recipe.RecipeCategory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCraftMasteryMenu extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/craftmastery_menu.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 202;

    private final EntityPlayer player;
    private List<TabButton> tabButtons;
    private List<AdminButton> adminButtons;
    private RecipeCategory currentCategory = RecipeCategory.ORDINARY;

    public GuiCraftMasteryMenu(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void initGui() {
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;

        tabButtons = new ArrayList<>();
        tabButtons.add(new TabButton(0, guiLeft - 28, guiTop + 10, 32, 28, "Ordinary", RecipeCategory.ORDINARY));
        tabButtons.add(new TabButton(1, guiLeft - 28, guiTop + 40, 32, 28, "Technical", RecipeCategory.TECHNICAL));
        tabButtons.add(new TabButton(2, guiLeft - 28, guiTop + 70, 32, 28, "Magical", RecipeCategory.MAGICAL));

        buttonList.addAll(tabButtons);

        if (player.isCreative() || player.canUseCommand(2, "")) {
            adminButtons = new ArrayList<>();
            adminButtons.add(new AdminButton(100, guiLeft + GUI_WIDTH, guiTop + 10, 20, 20, "+1", "Add new section"));
            adminButtons.add(new AdminButton(101, guiLeft + GUI_WIDTH, guiTop + 40, 20, 20, "+2", "Manage hidden tabs"));
            adminButtons.add(new AdminButton(102, guiLeft + GUI_WIDTH, guiTop + 70, 20, 20, "+3", "Manage recipes and conditions"));
            buttonList.addAll(adminButtons);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (TabButton tab : tabButtons) {
            if (tab.isMouseOver()) {
                drawHoveringText(tab.displayString, mouseX, mouseY);
            }
        }

        if (player.isCreative() || player.canUseCommand(2, "")) {
            for (AdminButton button : adminButtons) {
                if (button.isMouseOver()) {
                    drawHoveringText(button.tooltip, mouseX, mouseY);
                }
            }
        }

        // Here you would draw the recipes for the current category
        drawRecipes(guiLeft, guiTop);
    }

    private void drawRecipes(int guiLeft, int guiTop) {
        // Implement drawing of recipes based on currentCategory
        // This is where you'd show the recipe tree, unlocked and locked recipes, etc.
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof TabButton) {
            currentCategory = ((TabButton) button).category;
            // Refresh the recipe display
        } else if (button instanceof AdminButton) {
            // Handle admin button actions
            switch (button.id) {
                case 100:
                    // Add new section logic
                    break;
                case 101:
                    // Manage hidden tabs logic
                    break;
                case 102:
                    // Manage recipes and conditions logic
                    break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private class TabButton extends GuiButton {
        private final RecipeCategory category;

        public TabButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, RecipeCategory category) {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
            this.category = category;
        }

        @Override
        public void drawButton(net.minecraft.client.Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(TEXTURE);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int i = this.getHoverState(this.hovered);
                this.drawTexturedModalRect(this.x, this.y, 0, 202 + i * 28, this.width, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
            }
        }
    }

    private class AdminButton extends GuiButton {
        private final String tooltip;

        public AdminButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String tooltip) {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
            this.tooltip = tooltip;
        }
    }
}
