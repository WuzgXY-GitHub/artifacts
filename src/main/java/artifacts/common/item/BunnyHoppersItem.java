package artifacts.common.item;

import artifacts.Artifacts;
import artifacts.client.render.model.curio.BunnyHoppersModel;
import artifacts.common.init.Items;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

public class BunnyHoppersItem extends ArtifactItem {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/curio/bunny_hoppers.png");

    public BunnyHoppersItem() {
        super(new Properties(), "bunny_hoppers");
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return Curio.createProvider(new Curio(this) {
            private Object model;

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (!livingEntity.world.isRemote && livingEntity.ticksExisted % 15 == 0) {
                    livingEntity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 39, 1, true, false));
                }
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            protected BunnyHoppersModel getModel() {
                if (model == null) {
                    model = new BunnyHoppersModel();
                }
                return (BunnyHoppersModel) model;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            protected ResourceLocation getTexture() {
                return TEXTURE;
            }
        });
    }

    @Mod.EventBusSubscriber(modid = Artifacts.MODID)
    @SuppressWarnings("unused")
    public static class Events {

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent event) {
            if (event.getEntityLiving() instanceof PlayerEntity && CuriosApi.getCuriosHelper().findEquippedCurio(Items.BUNNY_HOPPERS, event.getEntityLiving()).isPresent()) {
                event.getEntity().world.playSound(null, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), SoundEvents.ENTITY_RABBIT_HURT, SoundCategory.PLAYERS, 1, (event.getEntityLiving().getRNG().nextFloat() - event.getEntityLiving().getRNG().nextFloat()) * 0.2F + 1.0F);
            }
        }

        @SubscribeEvent
        public static void onLivingFall(LivingFallEvent event) {
            if (event.getEntityLiving() instanceof PlayerEntity && CuriosApi.getCuriosHelper().findEquippedCurio(Items.BUNNY_HOPPERS, event.getEntityLiving()).isPresent()) {
                event.setDamageMultiplier(0);
            }
        }
    }
}
