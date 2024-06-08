package org.betterx.betternether.entity.render;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.entity.EntityFirefly;
import org.betterx.betternether.entity.model.ModelEntityFirefly;
import org.betterx.betternether.registry.EntityRenderRegistry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;


class FireflyGlowFeatureRenderer extends RenderLayer<EntityFirefly, AgeableListModel<EntityFirefly>> {
    private static final int LIT = 15728880;
    //static final ModelEmpty emptyModel = new ModelEmpty();

    public FireflyGlowFeatureRenderer(RenderLayerParent<EntityFirefly, AgeableListModel<EntityFirefly>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(
            PoseStack matrices,
            MultiBufferSource vertices,
            int light,
            EntityFirefly livingEntity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        EntityModel<EntityFirefly> model = this.getParentModel();


        if (model instanceof ModelEntityFirefly) {
            ResourceLocation identifier = this.getTextureLocation(livingEntity);
            RenderType renderLayer = RenderPhaseAccessor.getFirefly(identifier);
            VertexConsumer vertexConsumer = vertices.getBuffer(renderLayer);

            int color = livingEntity.getColor();

            addViewAlignedGlow(matrices, vertexConsumer, color);


            ((ModelEntityFirefly) model).getGlowPart()
                                        .render(
                                                matrices,
                                                vertexConsumer,
                                                light,
                                                OverlayTexture.NO_OVERLAY,
                                                color
                                        );
            ((ModelEntityFirefly) model).getGlowPart()
                                        .render(
                                                matrices,
                                                vertexConsumer,
                                                light,
                                                OverlayTexture.NO_OVERLAY,
                                                color
                                        );

        }
    }

    private void addViewAlignedGlow(
            PoseStack matrices,
            VertexConsumer vertexConsumer,
            int color
    ) {
        matrices.pushPose();

			/* //Original transform
			matrixStack.translate(0, 0.125, 0);
			matrixStack.multiply(this.dispatcher.getRotation());
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));*/

        matrices.translate(0, 1.25, 0);

        //Get inverse rotation to make view-aligned
        Matrix3f normalMatrix = matrices.last().normal();
        normalMatrix.transpose();
//        Triple<Quaternion, Vector3f, Quaternion> trip = normalMatrix.svdDecompose();
//        matrices.mulPose(trip.getLeft());
        matrices.mulPose(normalMatrix.transpose(new Matrix3f()).getNormalizedRotation(new Quaternionf()));

        PoseStack.Pose entry = matrices.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        addVertex(matrix4f, entry, vertexConsumer, -1, -1, 0F, 0.5F, color);
        addVertex(matrix4f, entry, vertexConsumer, 1, -1, 1F, 0.5F, color);
        addVertex(matrix4f, entry, vertexConsumer, 1, 1, 1F, 1F, color);
        addVertex(matrix4f, entry, vertexConsumer, -1, 1, 0F, 1F, color);

        //emptyModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue,  1f);
        matrices.popPose();
    }

    public static void addVertex(
            Matrix4f matrix4f,
            PoseStack.Pose matrix3f,
            VertexConsumer vertexConsumer,
            float posX,
            float posY,
            float u,
            float v,
            int color
    ) {
        vertexConsumer
                .addVertex(matrix4f, posX, posY, 0)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LIT)
                .setNormal(matrix3f, 0, 1, 0);
    }
}

public class RenderFirefly extends MobRenderer<EntityFirefly, AgeableListModel<EntityFirefly>> {
    private static final ResourceLocation TEXTURE = BetterNether.C.mk(
            "textures/entity/firefly.png"
    );

    public RenderFirefly(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelEntityFirefly(ctx.bakeLayer(EntityRenderRegistry.FIREFLY_MODEL)), 0);

        this.addLayer(new FireflyGlowFeatureRenderer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFirefly entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityFirefly entity, BlockPos blockPos) {
        return 15;
    }
}
