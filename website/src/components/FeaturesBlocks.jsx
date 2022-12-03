import React, {useState} from 'react';
import {
  FilterOrganization,
  Note,
  WarningSign,
} from "./FeatureIcons";
import Modal from "../utils/Modal";
import StackImage from '@site/static/docimages/stackbig.png';

function FeatureBlock(name, description, svgBody){
  return(
  <div className="relative flex flex-col items-center" data-aos="fade-up" data-aos-anchor="[data-aos-id-blocks]">
    {svgBody()}
    <h4 className="h4 mb-2">{name}</h4>
    <p className="text-lg text-gray-700 dark:text-gray-400 text-center">{description}</p>
  </div>
  )
}

function FeaturesBlocks() {
  const [videoModalOpen, setVideoModalOpen] = useState(false);
  return (
    <section>
      <div className="max-w-6xl mx-auto px-4 sm:px-6">
        <div className="pt-10 pb-12 md:pt-16 md:pb-20 border-t border-gray-800">

          {/* Section header */}
          <div className="max-w-3xl mx-auto text-center pb-12 md:pb-20">
            <h2 className="h2 mb-4">Production KMM stability monitoring</h2>
            <p className="text-xl text-gray-700 dark:text-gray-400">Report stability issues in your mobile applications directly from common Kotlin code.</p>
          </div>

          {/* Items */}
          <div className="max-w-sm mx-auto grid gap-8 md:grid-cols-2 lg:grid-cols-3 lg:gap-16 items-start md:max-w-2xl lg:max-w-none" data-aos-id-blocks>

            {FeatureBlock(
                "Symbolication",
                "iOS crash handling is different between Swift and Kotlin. CrashKiOS properly symbolicates Kotlin stack traces.",
                FilterOrganization
            )}

            {FeatureBlock(
                "Metadata",
                "Log breadcrumbs and custom key/value data directly from common Kotlin code to both platforms.",
                Note
            )}

            {FeatureBlock(
                "Fatal Crashes",
                "Captures fatal crashes as well as handled exception reports.",
                WarningSign
            )}

          </div>

          <div>
            <div className="relative flex justify-center items-center pt-20" data-aos="fade-up" data-aos-delay="200">
              <a className=" group" href="#0" onClick={(e) => { e.preventDefault(); e.stopPropagation(); setVideoModalOpen(true); }} aria-controls="modal">
                <img className="mx-auto auto-height" src={StackImage} width="855" height="430" alt="Hero" />
              </a>
            </div>

            {/* Modal */}
            <Modal id="modal" ariaLabel="modal-headline" show={videoModalOpen} handleClose={() => setVideoModalOpen(false)}>
              <div className="relative">
                <img className="mx-auto auto-height w-full" src={StackImage} alt="Hero" />
              </div>
            </Modal>

          </div>
        </div>
      </div>
    </section>
  );
}

export default FeaturesBlocks;
