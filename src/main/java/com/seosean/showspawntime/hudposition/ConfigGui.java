package com.seosean.showspawntime.hudposition;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.hudposition.autosplits.AutoSplitsApi;
import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.seosean.showspawntime.ShowSpawnTime.*;

public class ConfigGui extends GuiScreen {
   private int selected = -1;
   private int scroll = 0;
   private boolean isDragging;
   private Point dragOffset;
   private List<HudCoordinate> boxes = new ArrayList<>();

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();

      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

      int button = Mouse.getEventButton();
      boolean mouseButtonDown = Mouse.getEventButtonState();

      for (HudCoordinate box : boxes) {
         if (box.isMouseOver(mouseX, mouseY)) {
            if (button == 0) { // 左键
               if (mouseButtonDown) { // 鼠标按下
                  box.onMousePressed(mouseX, mouseY);
               } else { // 鼠标释放
                  box.onMouseReleased();
               }
            }
         }

         if (box.isDragging) { // 鼠标拖动
            box.onMouseDragged(mouseX, mouseY);
         }
      }
   }

   public void initGui() {
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Pre(this, buttonList));
      super.initGui();
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this, buttonList));
      int widthTime = this.fontRendererObj.getStringWidth("➤ W2 00:00");
      int widthPowerup = this.fontRendererObj.getStringWidth("-BONUS GOLD - 00:00 ");
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 82, sr.getScaledHeight() - 25, 80, 20, "Done"));
      this.buttonList.add(new GuiButton(2, sr.getScaledWidth() / 2 - 82 + 85, sr.getScaledHeight() - 25, 80, 20, "Reset"));
      boxes.clear();
      HudCoordinate boxSpawnTime = new HudCoordinate(0, ShowSpawnTime.getInstance().getXSpawnTime(), ShowSpawnTime.getInstance().getYSpawnTime(), widthTime, this.fontRendererObj.FONT_HEIGHT * 6, this.width, this.height);
      boxes.add(boxSpawnTime);
      HudCoordinate boxPowerup = new HudCoordinate(1, ShowSpawnTime.getInstance().getXPowerup(), ShowSpawnTime.getInstance().getYPowerup(), widthPowerup, this.fontRendererObj.FONT_HEIGHT * 6, this.width, this.height);
      boxes.add(boxPowerup);
      if(AutoSplitsApi.isAutoSplitsInstalled() && AutoSplitsApi.ApiManager()) {
         int widthSplits = this.fontRendererObj.getStringWidth("0:00:0");
         HudCoordinate boxSplits = new HudCoordinate(2, AutoSplitsApi.XSplitter, AutoSplitsApi.YSplitter, widthSplits, this.fontRendererObj.FONT_HEIGHT, this.width, this.height);
         boxes.add(boxSplits);
      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int p_mouseClicked_3_) throws IOException {
      super.mouseClicked(mouseX, mouseY, p_mouseClicked_3_);
//      ScaledResolution sr = new ScaledResolution(this.mc);
//      int minY = 10 + this.fontRendererObj.FONT_HEIGHT + 10;
//      int maxY = sr.getScaledHeight() - 25 - 10;
//      if (mouseY >= minY && mouseY <= maxY) {
//         int selectedRender = (mouseY - minY) / (5 + this.fontRendererObj.FONT_HEIGHT + 5);
//         this.selected = selectedRender + this.scroll;
//      }
   }

   protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
      super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
   }

   protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
      super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
   }

   public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawDefaultBackground();
      for (HudCoordinate box : boxes) {
         box.draw(this);
      }
      this.fontRendererObj.drawStringWithShadow("ShowSpawnTime", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("ShowSpawnTime") / 2.0F, 10.0F, Color.WHITE.getRGB());
      this.fontRendererObj.drawStringWithShadow("Click \"Done\" to save your current HUD position settings.", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("Click \"Done\" to save your current HUD position settings.") / 2.0F, this.height/2, Color.WHITE.getRGB());

      super.drawScreen(mouseX, mouseY, p_drawScreen_3_);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
         case 1: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  XSpawnTime = box.x;
                  YSpawnTime = box.y;
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1).set(box.x);
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1).set(box.y);
               } else if (box.getContents() == 1) {
                  XPowerup = box.x;
                  YPowerup = box.y;
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XPowerup", -1).set(box.x);
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YPowerup", -1).set(box.y);
               } else if (box.getContents() == 2) {
                  XSplitter = box.x;
                  YSplitter = box.y;
                  ConfigChangedEvent event = new ConfigChangedEvent.OnConfigChangedEvent(ShowSpawnTime.MODID, "dummyID", false, false);
                  MinecraftForge.EVENT_BUS.post(event);
                  if (!event.getResult().equals(Event.Result.DENY)) {
                     MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(ShowSpawnTime.MODID, "dummyID", false, false));
                  }
               }
            }
            ShowSpawnTime.getInstance().getConfig().save();
            this.mc.displayGuiScreen(null);
            break;
         }
         case 2: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1).set(-1);
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1).set(-1);
                  XSpawnTime = -1;
                  YSpawnTime = -1;
                  new DelayedTask(() -> {
                     box.x = ShowSpawnTime.getInstance().getXSpawnTime();
                     box.absoluteX = (int)(ShowSpawnTime.getInstance().getXSpawnTime() * this.width);
                     box.y = ShowSpawnTime.getInstance().getYSpawnTime();
                     box.absoluteY = (int)(ShowSpawnTime.getInstance().getYSpawnTime() * this.height);
                  }, 2);
               } else if (box.getContents() == 1) {
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XPowerup", -1).set(-1);
                  ShowSpawnTime.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YPowerup", -1).set(-1);
                  XPowerup = -1;
                  YPowerup = -1;
                  new DelayedTask(() -> {
                     box.x = ShowSpawnTime.getInstance().getXPowerup();
                     box.absoluteX = (int)(ShowSpawnTime.getInstance().getXPowerup() * this.width);
                     box.y = ShowSpawnTime.getInstance().getYPowerup();
                     box.absoluteY = (int)(ShowSpawnTime.getInstance().getYPowerup() * this.height);
                  }, 2);
               } else if (box.getContents() == 2) {
                  XSplitter = -1;
                  YSplitter = -1;
                  new DelayedTask(() -> {
                     box.x = ShowSpawnTime.getInstance().getXSplitter();
                     box.absoluteX = (int)(ShowSpawnTime.getInstance().getXSplitter() * this.width);
                     box.y = ShowSpawnTime.getInstance().getYSplitter();
                     box.absoluteY = (int)(ShowSpawnTime.getInstance().getYSplitter() * this.height);
                  }, 2);
               }
            }
            ShowSpawnTime.getInstance().getConfig().save();
            break;
         }
         default: {
            ShowSpawnTime.getInstance().getConfig().save();
            break;
         }
      }
   }

   public void onGuiClosed() {
      super.onGuiClosed();
   }
}
