import React, {useEffect} from 'react';
import Layout from '@theme/Layout';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Process from '@site/src/components/Process';
import HeroHome from '@site/src/components/HeroHome';
import Newsletter from '@site/src/components/Newsletter';
import AOS from 'aos';
import 'aos/dist/aos.css';

export default function Home(): JSX.Element {
  const {siteConfig} = useDocusaurusContext();

  useEffect(() => {
    AOS.init({
      once: true,
      disable: 'phone',
      duration: 350,
      easing: 'ease-out-sine',
    });
  });

  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <HeroHome />
      <Process/>
      <Newsletter/>
      <div>&nbsp;</div>
      <div>&nbsp;</div>
    </Layout>
  );
}
